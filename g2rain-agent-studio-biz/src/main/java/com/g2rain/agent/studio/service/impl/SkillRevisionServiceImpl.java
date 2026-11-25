package com.g2rain.agent.studio.service.impl;

import com.g2rain.agent.studio.converter.SkillRevisionConverter;
import com.g2rain.agent.studio.dao.SkillProfileDao;
import com.g2rain.agent.studio.dao.SkillRevisionDao;
import com.g2rain.agent.studio.dao.SkillToolBindingDao;
import com.g2rain.agent.studio.dao.po.SkillProfilePo;
import com.g2rain.agent.studio.dao.po.SkillRevisionPo;
import com.g2rain.agent.studio.dao.po.SkillToolBindingPo;
import com.g2rain.agent.studio.dto.CreateSkillRevisionDraftDto;
import com.g2rain.agent.studio.dto.PublishSkillRevisionDto;
import com.g2rain.agent.studio.dto.SkillRevisionDto;
import com.g2rain.agent.studio.dto.SkillRevisionSelectDto;
import com.g2rain.agent.studio.dto.SkillToolBindingSelectDto;
import com.g2rain.agent.studio.enums.EnabledFlag;
import com.g2rain.agent.studio.enums.RevisionOrigin;
import com.g2rain.agent.studio.enums.RevisionStatus;
import com.g2rain.agent.studio.enums.StudioErrorCode;
import com.g2rain.agent.studio.service.SkillRevisionService;
import com.g2rain.agent.studio.vo.SkillRevisionVo;
import com.g2rain.common.exception.BusinessException;
import com.g2rain.common.exception.SystemErrorCode;
import com.g2rain.common.id.IdGenerator;
import com.g2rain.common.json.JsonCodec;
import com.g2rain.common.json.JsonCodecFactory;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.utils.Asserts;
import com.g2rain.common.utils.Collections;
import com.g2rain.common.utils.Moments;
import com.g2rain.common.utils.Strings;
import com.g2rain.common.validation.Validations;
import com.g2rain.mybatis.pagination.PageContext;
import com.g2rain.mybatis.pagination.model.Page;
import jakarta.annotation.Resource;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.type.TypeReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Skill 修订表服务实现类
 * 表名: skill_revision
 *
 * @author G2rain Generator
 */
@Service(value = "skillRevisionServiceImpl")
public class SkillRevisionServiceImpl implements SkillRevisionService {

    private static final JsonCodec JSON = JsonCodecFactory.instance();

    @Resource(name = "skillRevisionDao")
    private SkillRevisionDao skillRevisionDao;

    @Resource(name = "skillProfileDao")
    private SkillProfileDao skillProfileDao;

    @Resource(name = "skillToolBindingDao")
    private SkillToolBindingDao skillToolBindingDao;

    private IdGenerator idGenerator;

    @Qualifier("idGenerator")
    @Autowired(required = false)
    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public List<SkillRevisionVo> selectList(SkillRevisionSelectDto selectDto) {
        return skillRevisionDao.selectList(selectDto)
            .stream()
            .map(SkillRevisionConverter.INSTANCE::po2vo)
            .toList();
    }

    @Override
    public PageData<SkillRevisionVo> selectPage(PageSelectListDto<SkillRevisionSelectDto> selectDto) {
        Page<SkillRevisionPo> page = PageContext.of(selectDto.getPageNum(), selectDto.getPageSize(), () -> {
            skillRevisionDao.selectList(selectDto.getQuery());
        });
        List<SkillRevisionVo> result = page.getResult()
            .stream()
            .map(SkillRevisionConverter.INSTANCE::po2vo)
            .toList();
        return PageData.of(page.getPageNum(), page.getPageSize(), page.getTotal(), result);
    }

    @Override
    public SkillRevisionVo selectById(Long id) {
        SkillRevisionPo existing = skillRevisionDao.selectById(id);
        Asserts.isTrue(Objects.nonNull(existing), SystemErrorCode.DATA_NOT_EXISTS, id);
        return SkillRevisionConverter.INSTANCE.po2vo(existing);
    }

    @Override
    public Long save(SkillRevisionDto dto) {
        Validations.validateSave(dto);

        // 转换 DTO 为 PO
        SkillRevisionPo entity = SkillRevisionConverter.INSTANCE.dto2po(dto);

        // 判断是新增还是更新
        Long id = entity.getId();
        if (Objects.isNull(id) || id == 0) {
            // 新增：使用IdGenerator生成主键
            entity.setId(idGenerator.generateId());
            LocalDateTime now = Moments.now();
            entity.setUpdateTime(now);
            entity.setCreateTime(now);
            int success = skillRevisionDao.insert(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.CREATE_DATA_ERROR);
        } else {
            SkillRevisionPo existing = skillRevisionDao.selectById(id);
            Asserts.isTrue(Objects.nonNull(existing), SystemErrorCode.DATA_NOT_EXISTS, id);
            // 更新：直接更新
            entity.setUpdateTime(Moments.now());
            int success = skillRevisionDao.update(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.UPDATE_DATA_ERROR, id);
        }

        return entity.getId();
    }

    @Override
    public int delete(Long id) {
        SkillRevisionPo existing = skillRevisionDao.selectById(id);
        Asserts.isTrue(Objects.nonNull(existing), SystemErrorCode.DATA_NOT_EXISTS, id);
        return skillRevisionDao.delete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordPublishedSnapshot(Long skillId) {
        SkillProfilePo skill = skillProfileDao.selectByIdForUpdate(skillId);
        Asserts.isTrue(Objects.nonNull(skill), SystemErrorCode.DATA_NOT_EXISTS, skillId);

        skillRevisionDao.supersedePublished(skillId);
        int nextNo = skillRevisionDao.selectMaxRevisionNo(skillId) + 1;
        SkillRevisionPo row = newPublished(skill, loadToolNames(skillId), nextNo, RevisionOrigin.MANUAL, null);
        Asserts.greaterThan(skillRevisionDao.insert(row), 0, SystemErrorCode.CREATE_DATA_ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBySkillId(Long skillId) {
        skillRevisionDao.deleteBySkillId(skillId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDraft(CreateSkillRevisionDraftDto dto) {
        Validations.validateDefault(dto);
        SkillProfilePo skill = skillProfileDao.selectById(dto.getSkillId());
        Asserts.isTrue(Objects.nonNull(skill), SystemErrorCode.DATA_NOT_EXISTS, dto.getSkillId());

        SkillRevisionPo published = skillRevisionDao.selectPublished(dto.getSkillId());
        Asserts.isTrue(Objects.nonNull(published), SystemErrorCode.DATA_NOT_EXISTS, dto.getSkillId());

        SkillRevisionPo draft = new SkillRevisionPo();
        draft.setId(idGenerator.generateId());
        draft.setOrganId(skill.getOrganId());
        draft.setSkillId(skill.getId());
        draft.setRevisionNo(0);
        draft.setSkillName(Objects.requireNonNullElse(dto.getSkillName(), published.getSkillName()));
        draft.setSemanticDesc(Objects.nonNull(dto.getSemanticDesc()) ? dto.getSemanticDesc() : published.getSemanticDesc());
        draft.setSkillManual(Objects.nonNull(dto.getSkillManual()) ? dto.getSkillManual() : published.getSkillManual());
        draft.setSkillContract(Objects.nonNull(dto.getSkillContract()) ? dto.getSkillContract() : published.getSkillContract());
        draft.setToolNames(Objects.nonNull(dto.getToolNames())
            ? JSON.obj2str(dto.getToolNames())
            : published.getToolNames());
        draft.setStatus(RevisionStatus.DRAFT.name());
        draft.setOrigin(RevisionOrigin.CANDIDATE.name());
        draft.setBaseRevisionNo(published.getRevisionNo());
        draft.setNote(dto.getNote());
        draft.setDeleteFlag(Boolean.FALSE);
        LocalDateTime now = Moments.now();
        draft.setCreateTime(now);
        draft.setUpdateTime(now);
        draft.setVersion(0);
        Asserts.greaterThan(skillRevisionDao.insert(draft), 0, SystemErrorCode.CREATE_DATA_ERROR);
        return draft.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publish(Long id, PublishSkillRevisionDto dto) {
        Validations.validateDefault(dto);
        SkillRevisionPo draft = skillRevisionDao.selectById(id);
        Asserts.isTrue(Objects.nonNull(draft), SystemErrorCode.DATA_NOT_EXISTS, id);
        if (!RevisionStatus.DRAFT.name().equals(draft.getStatus())) {
            throw new BusinessException(StudioErrorCode.REVISION_STATUS_INVALID, id);
        }

        SkillProfilePo skill = skillProfileDao.selectByIdForUpdate(draft.getSkillId());
        Asserts.isTrue(Objects.nonNull(skill), SystemErrorCode.DATA_NOT_EXISTS, draft.getSkillId());

        SkillRevisionPo published = skillRevisionDao.selectPublished(draft.getSkillId());
        int currentNo = Objects.isNull(published) ? 0 : Objects.requireNonNullElse(published.getRevisionNo(), 0);
        if (!Objects.equals(dto.getExpectedRevisionNo(), currentNo)) {
            throw new BusinessException(StudioErrorCode.SKILL_MODIFIED, dto.getExpectedRevisionNo(), currentNo);
        }

        applySnapshot(skill, draft);
        if (Objects.nonNull(published)) {
            Asserts.greaterThan(
                skillRevisionDao.updateStatus(published.getId(), RevisionStatus.PUBLISHED.name(),
                    RevisionStatus.SUPERSEDED.name(), null),
                0, SystemErrorCode.UPDATE_DATA_ERROR, published.getId());
        }

        int nextNo = skillRevisionDao.selectMaxRevisionNo(draft.getSkillId()) + 1;
        Asserts.greaterThan(
            skillRevisionDao.updateStatus(id, RevisionStatus.DRAFT.name(), RevisionStatus.PUBLISHED.name(), nextNo),
            0, SystemErrorCode.UPDATE_DATA_ERROR, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void discard(Long id) {
        SkillRevisionPo draft = skillRevisionDao.selectById(id);
        Asserts.isTrue(Objects.nonNull(draft), SystemErrorCode.DATA_NOT_EXISTS, id);
        if (!RevisionStatus.DRAFT.name().equals(draft.getStatus())) {
            throw new BusinessException(StudioErrorCode.REVISION_STATUS_INVALID, id);
        }

        Asserts.greaterThan(
            skillRevisionDao.updateStatus(id, RevisionStatus.DRAFT.name(), RevisionStatus.DISCARDED.name(), null),
            0, SystemErrorCode.UPDATE_DATA_ERROR, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rollback(Long id) {
        SkillRevisionPo source = skillRevisionDao.selectById(id);
        Asserts.isTrue(Objects.nonNull(source), SystemErrorCode.DATA_NOT_EXISTS, id);
        if (RevisionStatus.DRAFT.name().equals(source.getStatus())) {
            throw new BusinessException(StudioErrorCode.REVISION_STATUS_INVALID, id);
        }

        SkillProfilePo skill = skillProfileDao.selectByIdForUpdate(source.getSkillId());
        Asserts.isTrue(Objects.nonNull(skill), SystemErrorCode.DATA_NOT_EXISTS, source.getSkillId());

        applySnapshot(skill, source);
        skillRevisionDao.supersedePublished(source.getSkillId());
        int nextNo = skillRevisionDao.selectMaxRevisionNo(source.getSkillId()) + 1;
        SkillRevisionPo row = newPublished(skill, parseToolNames(source.getToolNames()), nextNo,
            RevisionOrigin.ROLLBACK, source.getNote());
        // 快照内容以历史修订为准（内存中的 skill 行尚未刷新）
        row.setSkillName(source.getSkillName());
        row.setSemanticDesc(source.getSemanticDesc());
        row.setSkillManual(source.getSkillManual());
        row.setSkillContract(source.getSkillContract());
        row.setToolNames(source.getToolNames());
        Asserts.greaterThan(skillRevisionDao.insert(row), 0, SystemErrorCode.CREATE_DATA_ERROR);
    }

    /**
     * 把修订快照写回技能与工具绑定；不触发保存留痕。
     *
     * @param skill    已锁定的技能行
     * @param revision 快照来源
     */
    private void applySnapshot(SkillProfilePo skill, SkillRevisionPo revision) {
        SkillProfilePo update = new SkillProfilePo();
        update.setId(skill.getId());
        update.setSkillName(revision.getSkillName());
        update.setSemanticDesc(revision.getSemanticDesc());
        update.setSkillManual(revision.getSkillManual());
        update.setSkillContract(revision.getSkillContract());
        update.setUpdateTime(Moments.now());
        Asserts.greaterThan(skillProfileDao.update(update), 0, SystemErrorCode.UPDATE_DATA_ERROR, skill.getId());

        List<String> wanted = parseToolNames(revision.getToolNames());
        replaceToolBindings(skill, wanted);
    }

    /**
     * 按快照整体替换工具绑定：多出来的删掉，少的补上。
     *
     * @param skill  技能
     * @param wanted 目标工具名
     */
    private void replaceToolBindings(SkillProfilePo skill, List<String> wanted) {
        SkillToolBindingSelectDto select = new SkillToolBindingSelectDto();
        select.setSkillId(skill.getId());
        List<SkillToolBindingPo> current = skillToolBindingDao.selectList(select);
        Set<String> wantedSet = new HashSet<>(wanted);
        Set<String> existing = new HashSet<>();

        for (SkillToolBindingPo binding : current) {
            if (wantedSet.contains(binding.getToolName())) {
                existing.add(binding.getToolName());
                continue;
            }

            skillToolBindingDao.delete(binding.getId());
        }

        LocalDateTime now = Moments.now();
        for (String toolName : wanted) {
            if (existing.contains(toolName) || Strings.isBlank(toolName)) {
                continue;
            }

            SkillToolBindingPo row = new SkillToolBindingPo();
            row.setId(idGenerator.generateId());
            row.setOrganId(skill.getOrganId());
            row.setSkillId(skill.getId());
            row.setToolName(toolName);
            row.setEnabledFlag(EnabledFlag.YES.getCode());
            row.setDeleteFlag(Boolean.FALSE);
            row.setCreateTime(now);
            row.setUpdateTime(now);
            row.setVersion(0);
            Asserts.greaterThan(skillToolBindingDao.insert(row), 0, SystemErrorCode.CREATE_DATA_ERROR);
        }
    }

    /**
     * 组装一条发布修订行。
     *
     * @param skill     技能
     * @param toolNames 工具名
     * @param revisionNo 修订号
     * @param origin    来源
     * @param note      说明；可空
     * @return 待插入行
     */
    private SkillRevisionPo newPublished(SkillProfilePo skill, List<String> toolNames, int revisionNo,
                                         RevisionOrigin origin, @Nullable String note) {
        SkillRevisionPo row = new SkillRevisionPo();
        row.setId(idGenerator.generateId());
        row.setOrganId(skill.getOrganId());
        row.setSkillId(skill.getId());
        row.setRevisionNo(revisionNo);
        row.setSkillName(skill.getSkillName());
        row.setSemanticDesc(skill.getSemanticDesc());
        row.setSkillManual(skill.getSkillManual());
        row.setSkillContract(skill.getSkillContract());
        row.setToolNames(JSON.obj2str(toolNames));
        row.setStatus(RevisionStatus.PUBLISHED.name());
        row.setOrigin(origin.name());
        row.setNote(note);
        row.setDeleteFlag(Boolean.FALSE);
        LocalDateTime now = Moments.now();
        row.setCreateTime(now);
        row.setUpdateTime(now);
        row.setVersion(0);
        return row;
    }

    /**
     * 读取技能全部绑定工具名。
     *
     * @param skillId 技能主键
     * @return 工具名列表
     */
    private List<String> loadToolNames(Long skillId) {
        SkillToolBindingSelectDto select = new SkillToolBindingSelectDto();
        select.setSkillId(skillId);
        List<SkillToolBindingPo> bindings = skillToolBindingDao.selectList(select);
        if (Collections.isEmpty(bindings)) {
            return List.of();
        }

        List<String> names = new ArrayList<>(bindings.size());
        for (SkillToolBindingPo binding : bindings) {
            if (Strings.isNotBlank(binding.getToolName())) {
                names.add(binding.getToolName());
            }
        }
        return names;
    }

    /**
     * 解析快照中的工具名 JSON。
     *
     * @param json 工具名数组 JSON；可空
     * @return 工具名列表
     */
    private List<String> parseToolNames(@Nullable String json) {
        if (Strings.isBlank(json)) {
            return List.of();
        }

        List<String> names = JSON.str2obj(json, new TypeReference<List<String>>() {
        });
        return Objects.requireNonNullElse(names, List.of());
    }
}
