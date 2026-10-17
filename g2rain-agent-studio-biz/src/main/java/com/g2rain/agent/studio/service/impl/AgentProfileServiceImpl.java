package com.g2rain.agent.studio.service.impl;

import com.g2rain.agent.studio.converter.AgentProfileConverter;
import com.g2rain.agent.studio.dao.AgentProfileDao;
import com.g2rain.agent.studio.dao.SkillProfileDao;
import com.g2rain.agent.studio.dao.SkillToolBindingDao;
import com.g2rain.agent.studio.dao.po.AgentProfilePo;
import com.g2rain.agent.studio.dao.po.SkillProfilePo;
import com.g2rain.agent.studio.dao.po.SkillToolBindingPo;
import com.g2rain.agent.studio.dto.AgentProfileDto;
import com.g2rain.agent.studio.dto.AgentProfileSelectDto;
import com.g2rain.agent.studio.dto.UpdateStatusDto;
import com.g2rain.agent.studio.enums.ResourceStatus;
import com.g2rain.agent.studio.dto.SkillProfileSelectDto;
import com.g2rain.agent.studio.dto.SkillToolBindingSelectDto;
import com.g2rain.agent.studio.enums.EnabledFlag;
import com.g2rain.agent.studio.enums.ResourceStatus;
import com.g2rain.agent.studio.enums.ResourceType;
import com.g2rain.agent.studio.service.AgentProfileService;
import com.g2rain.agent.studio.vo.AgentBundleVo;
import com.g2rain.agent.studio.vo.AgentCatalogItemVo;
import com.g2rain.agent.studio.vo.AgentOptionVo;
import com.g2rain.agent.studio.vo.AgentProfileVo;
import com.g2rain.agent.studio.vo.SkillDigestVo;
import com.g2rain.common.concurrent.ContextExecutors;
import com.g2rain.common.exception.SystemErrorCode;
import com.g2rain.common.id.IdGenerator;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.utils.Asserts;
import com.g2rain.common.utils.Collections;
import com.g2rain.common.utils.Moments;
import com.g2rain.common.validation.Validations;
import com.g2rain.mybatis.pagination.PageContext;
import com.g2rain.mybatis.pagination.model.Page;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 数字员工画像表服务实现类
 * 表名: agent_profile
 *
 * @author G2rain Generator
 */
@Service(value = "agentProfileServiceImpl")
public class AgentProfileServiceImpl implements AgentProfileService {

    @Resource(name = "agentProfileDao")
    private AgentProfileDao agentProfileDao;

    @Resource(name = "skillToolBindingDao")
    private SkillToolBindingDao skillToolBindingDao;

    @Resource(name = "skillProfileDao")
    private SkillProfileDao skillProfileDao;

    private IdGenerator idGenerator;

    @Qualifier("idGenerator")
    @Autowired(required = false)
    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public List<AgentProfileVo> selectList(AgentProfileSelectDto selectDto) {
        return agentProfileDao.selectList(selectDto)
            .stream()
            .map(AgentProfileConverter.INSTANCE::po2vo)
            .toList();
    }

    @Override
    public List<AgentProfileVo> selectPlatformList(AgentProfileSelectDto selectDto) {
        AgentProfileSelectDto query = selectDto != null ? selectDto : new AgentProfileSelectDto();
        query.setResourceType(ResourceType.PLATFORM.name());
        query.setOrganId(null);
        return agentProfileDao.selectListWithoutIsolation(query)
            .stream()
            .map(AgentProfileConverter.INSTANCE::po2vo)
            .toList();
    }

    /**
     * 查询 ACTIVE 数字员工目录（PLATFORM 不隔离 + CUSTOM 本租户）。
     *
     * <p>PLATFORM 走 {@code selectListWithoutIsolation}，CUSTOM 走租户隔离 {@code selectList}，
     * 合并后按 id 排序再转 {@link AgentCatalogItemVo}。
     *
     * @return ACTIVE Agent 摘要列表
     */
    @Override
    public List<AgentCatalogItemVo> selectActiveCatalog() {
        return Stream.of(
                ContextExecutors.callVirtual(() -> {
                    AgentProfileSelectDto select = new AgentProfileSelectDto();
                    select.setResourceType(ResourceType.PLATFORM.name());
                    select.setStatus(ResourceStatus.ACTIVE.name());
                    return agentProfileDao.selectListWithoutIsolation(select);
                }),
                ContextExecutors.callVirtual(() -> {
                    AgentProfileSelectDto select = new AgentProfileSelectDto();
                    select.setResourceType(ResourceType.CUSTOM.name());
                    select.setStatus(ResourceStatus.ACTIVE.name());
                    return agentProfileDao.selectList(select);
                })
            )
            .map(CompletableFuture::join)
            .flatMap(List::stream)
            .sorted(Comparator.comparing(AgentProfilePo::getId, Comparator.nullsLast(Long::compareTo)))
            .map(AgentProfileConverter.INSTANCE::po2catalog)
            .toList();
    }

    /**
     * 查询 ACTIVE 数字员工下拉选项（PLATFORM 不隔离 + CUSTOM 本租户）。
     *
     * <p>查询策略与 {@link #selectActiveCatalog()} 相同，mapper 仅取 id / agent_name。
     *
     * @return ACTIVE Agent 选项列表
     */
    @Override
    public List<AgentOptionVo> selectActiveOptions() {
        return Stream.of(
                ContextExecutors.callVirtual(() -> {
                    AgentProfileSelectDto select = new AgentProfileSelectDto();
                    select.setResourceType(ResourceType.PLATFORM.name());
                    select.setStatus(ResourceStatus.ACTIVE.name());
                    return agentProfileDao.selectOptionsListWithoutIsolation(select);
                }),
                ContextExecutors.callVirtual(() -> {
                    AgentProfileSelectDto select = new AgentProfileSelectDto();
                    select.setResourceType(ResourceType.CUSTOM.name());
                    select.setStatus(ResourceStatus.ACTIVE.name());
                    return agentProfileDao.selectOptionsList(select);
                })
            )
            .map(CompletableFuture::join)
            .flatMap(List::stream)
            .sorted(Comparator.comparing(AgentProfilePo::getId, Comparator.nullsLast(Long::compareTo)))
            .map(AgentProfileConverter.INSTANCE::po2option)
            .toList();
    }

    /**
     * 按 agentId 解析画像（含 status，不筛 ACTIVE）。
     *
     * <p>先本租户 CUSTOM，再 PLATFORM 不隔离；不按 ACTIVE 过滤，status 原样返回。
     *
     * @param agentId 数字员工标识
     * @return 画像摘要；不存在为 null
     */
    @Override
    public AgentCatalogItemVo resolveProfile(Long agentId) {
        AgentProfileSelectDto select = new AgentProfileSelectDto();
        select.setId(agentId);
        select.setResourceType(ResourceType.CUSTOM.name());
        var agentProfiles = agentProfileDao.selectList(select);
        if (Collections.isNotEmpty(agentProfiles)) {
            return AgentProfileConverter.INSTANCE.po2catalog(
                agentProfiles.getFirst()
            );
        }

        select.setResourceType(ResourceType.PLATFORM.name());
        agentProfiles = agentProfileDao.selectListWithoutIsolation(select);
        if (Collections.isEmpty(agentProfiles)) {
            return null;
        }

        return AgentProfileConverter.INSTANCE.po2catalog(
            agentProfiles.getFirst()
        );
    }

    /**
     * 按 agentId 查询 ACTIVE Skill 短目录（不含手册 / 契约 / tools）。
     *
     * @param agentId 数字员工标识
     * @return Skill 短目录；无技能为空列表
     */
    @Override
    public List<SkillDigestVo> selectSkillDigest(Long agentId) {
        return loadActiveDigestSkills(agentId).stream()
            .map(skill -> {
                SkillDigestVo item = new SkillDigestVo();
                item.setSkillKey(skill.getSkillKey());
                item.setSkillName(skill.getSkillName());
                item.setSemanticDesc(skill.getSemanticDesc());
                return item;
            })
            .toList();
    }

    /**
     * 按 agentId 加载 ACTIVE Skill 短字段（CUSTOM 隔离 + PLATFORM 不隔离）。
     */
    private List<SkillProfilePo> loadActiveDigestSkills(Long agentId) {
        SkillProfileSelectDto custom = new SkillProfileSelectDto();
        custom.setAgentId(agentId);
        custom.setResourceType(ResourceType.CUSTOM.name());
        custom.setStatus(ResourceStatus.ACTIVE.name());
        SkillProfileSelectDto platform = new SkillProfileSelectDto();
        platform.setAgentId(agentId);
        platform.setResourceType(ResourceType.PLATFORM.name());
        platform.setStatus(ResourceStatus.ACTIVE.name());
        return Stream.concat(
            skillProfileDao.selectDigestList(custom).stream(),
            skillProfileDao.selectDigestListWithoutIsolation(platform).stream()
        ).toList();
    }

    /**
     * 按 agentId 查询能力包：Skill（手册/契约）+ 其下 Tool 列表。
     *
     * @param agentId 数字员工标识
     * @return 能力包；无技能时 skills 为空列表
     */
    @Override
    public AgentBundleVo selectCapabilityBundle(Long agentId) {
        List<SkillProfilePo> skillProfiles = loadActiveSkills(agentId);
        if (Collections.isEmpty(skillProfiles)) {
            return new AgentBundleVo(List.of());
        }

        Set<Long> skillIds = skillProfiles.stream()
            .map(SkillProfilePo::getId)
            .filter(Objects::nonNull)
            .collect(Collectors.toCollection(HashSet::new));
        SkillToolBindingSelectDto toolSelect = new SkillToolBindingSelectDto();
        toolSelect.setSkillIds(skillIds);
        toolSelect.setEnabledFlag(EnabledFlag.YES.getCode());
        var toolBindings = skillToolBindingDao.selectListWithoutIsolation(toolSelect);

        Map<Long, List<AgentBundleVo.ToolBriefVo>> toolsBySkill = toolBindings.stream()
            .collect(Collectors.groupingBy(
                SkillToolBindingPo::getSkillId,
                Collectors.mapping(b -> {
                    AgentBundleVo.ToolBriefVo t = new AgentBundleVo.ToolBriefVo();
                    t.setToolName(b.getToolName());
                    return t;
                }, Collectors.toList())
            ));

        List<AgentBundleVo.SkillBriefVo> skills = skillProfiles.stream()
            .map(skill -> {
                AgentBundleVo.SkillBriefVo brief = new AgentBundleVo.SkillBriefVo();
                brief.setSkillKey(skill.getSkillKey());
                brief.setSkillName(skill.getSkillName());
                brief.setSemanticDesc(skill.getSemanticDesc());
                brief.setSkillManual(skill.getSkillManual());
                brief.setSkillContract(skill.getSkillContract());
                brief.setTools(toolsBySkill.getOrDefault(skill.getId(), List.of()));
                return brief;
            })
            .toList();

        return new AgentBundleVo(skills);
    }

    /**
     * 按 agentId 加载 ACTIVE Skill 完整字段（CUSTOM 隔离 + PLATFORM 不隔离）。
     */
    private List<SkillProfilePo> loadActiveSkills(Long agentId) {
        SkillProfileSelectDto custom = new SkillProfileSelectDto();
        custom.setAgentId(agentId);
        custom.setResourceType(ResourceType.CUSTOM.name());
        custom.setStatus(ResourceStatus.ACTIVE.name());
        SkillProfileSelectDto platform = new SkillProfileSelectDto();
        platform.setAgentId(agentId);
        platform.setResourceType(ResourceType.PLATFORM.name());
        platform.setStatus(ResourceStatus.ACTIVE.name());
        return Stream.concat(
            skillProfileDao.selectList(custom).stream(),
            skillProfileDao.selectListWithoutIsolation(platform).stream()
        ).toList();
    }

    @Override
    public PageData<AgentProfileVo> selectPage(PageSelectListDto<AgentProfileSelectDto> selectDto) {
        Page<AgentProfilePo> page = PageContext.of(selectDto.getPageNum(), selectDto.getPageSize(), () -> {
            agentProfileDao.selectList(selectDto.getQuery());
        });
        List<AgentProfileVo> result = page.getResult()
            .stream()
            .map(AgentProfileConverter.INSTANCE::po2vo)
            .toList();
        return PageData.of(page.getPageNum(), page.getPageSize(), page.getTotal(), result);
    }

    @Override
    public PageData<AgentProfileVo> selectPlatformPage(PageSelectListDto<AgentProfileSelectDto> selectDto) {
        AgentProfileSelectDto query = selectDto.getQuery();
        if (Objects.isNull(query)) {
            query = new AgentProfileSelectDto();
            selectDto.setQuery(query);
        }
        query.setResourceType(ResourceType.PLATFORM.name());
        query.setOrganId(null);
        Page<AgentProfilePo> page = PageContext.of(selectDto.getPageNum(), selectDto.getPageSize(), () ->
            agentProfileDao.selectListWithoutIsolation(selectDto.getQuery())
        );
        List<AgentProfileVo> result = page.getResult()
            .stream()
            .map(AgentProfileConverter.INSTANCE::po2vo)
            .toList();
        return PageData.of(page.getPageNum(), page.getPageSize(), page.getTotal(), result);
    }

    @Override
    public Long save(AgentProfileDto dto) {
        Validations.validateSave(dto);

        // 转换 DTO 为 PO
        AgentProfilePo entity = AgentProfileConverter.INSTANCE.dto2po(dto);

        // 判断是新增还是更新
        Long id = entity.getId();
        if (Objects.isNull(id) || id == 0) {
            // 同机构下 agentCode 唯一
            AgentProfileSelectDto codeSelect = new AgentProfileSelectDto();
            codeSelect.setOrganId(dto.getOrganId());
            codeSelect.setAgentCode(dto.getAgentCode());
            Asserts.isTrue(Collections.isEmpty(agentProfileDao.selectList(codeSelect)),
                SystemErrorCode.DATA_EXISTS, dto.getAgentCode());

            // 新增：使用IdGenerator生成主键；状态固定 ACTIVE（启停走 updateStatus）
            entity.setId(idGenerator.generateId());
            entity.setStatus(ResourceStatus.ACTIVE.name());
            LocalDateTime now = Moments.now();
            entity.setUpdateTime(now);
            entity.setCreateTime(now);
            int success = agentProfileDao.insert(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.CREATE_DATA_ERROR);
        } else {
            AgentProfilePo existing = agentProfileDao.selectById(id);
            Asserts.isTrue(Objects.nonNull(existing), SystemErrorCode.DATA_NOT_EXISTS, id);
            // 更新：不允许改状态、编码（启停 / 改键另走）
            entity.setStatus(null);
            entity.setAgentCode(null);
            entity.setUpdateTime(Moments.now());
            Asserts.greaterThan(agentProfileDao.update(entity), 0, SystemErrorCode.UPDATE_DATA_ERROR, id);
        }

        return entity.getId();
    }

    /**
     * 删除智能体：级联删除其下 Skill 及各 Skill 的 Tool 绑定。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long id) {
        AgentProfilePo existing = agentProfileDao.selectById(id);
        Asserts.isTrue(Objects.nonNull(existing), SystemErrorCode.DATA_NOT_EXISTS, id);

        skillToolBindingDao.deleteByAgentId(id);
        skillProfileDao.deleteByAgentId(id);
        return agentProfileDao.delete(id);
    }

    @Override
    public int updateStatus(Long id, UpdateStatusDto dto) {
        ResourceStatus.validate(dto.getStatus());

        AgentProfilePo existing = agentProfileDao.selectById(id);
        Asserts.isTrue(Objects.nonNull(existing), SystemErrorCode.DATA_NOT_EXISTS, id);

        AgentProfilePo entity = new AgentProfilePo();
        entity.setId(id);
        entity.setStatus(dto.getStatus());
        entity.setUpdateTime(Moments.now());
        int success = agentProfileDao.update(entity);
        Asserts.greaterThan(success, 0, SystemErrorCode.UPDATE_DATA_ERROR, id);
        return success;
    }
}
