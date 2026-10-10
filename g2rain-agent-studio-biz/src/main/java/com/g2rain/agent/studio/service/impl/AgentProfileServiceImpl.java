package com.g2rain.agent.studio.service.impl;

import com.g2rain.agent.studio.converter.AgentProfileConverter;
import com.g2rain.agent.studio.dao.AgentProfileDao;
import com.g2rain.agent.studio.dao.AgentSkillBindingDao;
import com.g2rain.agent.studio.dao.SkillProfileDao;
import com.g2rain.agent.studio.dao.SkillToolBindingDao;
import com.g2rain.agent.studio.dao.po.AgentProfilePo;
import com.g2rain.agent.studio.dao.po.AgentSkillBindingPo;
import com.g2rain.agent.studio.dao.po.SkillProfilePo;
import com.g2rain.agent.studio.dao.po.SkillToolBindingPo;
import com.g2rain.agent.studio.dto.AgentProfileDto;
import com.g2rain.agent.studio.dto.AgentProfileSelectDto;
import com.g2rain.agent.studio.dto.AgentSkillBindingSelectDto;
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

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
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

    @Resource(name = "agentSkillBindingDao")
    private AgentSkillBindingDao agentSkillBindingDao;

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
     * 按 agentId 查询绑定 Skill 短目录（不含手册 / 契约 / tools）。
     *
     * <p>读启用中的 Agent-Skill 绑定，再经 {@link #loadActiveDigestSkills} 拉短字段组装。
     *
     * @param agentId 数字员工标识
     * @return Skill 短目录；无绑定为空列表
     */
    @Override
    public List<SkillDigestVo> selectSkillDigest(Long agentId) {
        AgentSkillBindingSelectDto bindingSelect = new AgentSkillBindingSelectDto();
        bindingSelect.setAgentId(agentId);
        bindingSelect.setEnabledFlag(EnabledFlag.YES.getCode());
        List<AgentSkillBindingPo> bindings = agentSkillBindingDao.selectList(bindingSelect);
        if (Collections.isEmpty(bindings)) {
            return List.of();
        }

        Map<Long, SkillProfilePo> skillMap = loadActiveDigestSkills(bindings);
        return bindings.stream()
            .map(binding -> {
                SkillProfilePo skill = skillMap.get(binding.getSkillId());
                if (skill == null) {
                    return null;
                }
                SkillDigestVo item = new SkillDigestVo();
                item.setSkillKey(skill.getSkillKey());
                item.setSkillName(skill.getSkillName());
                item.setSemanticDesc(skill.getSemanticDesc());
                return item;
            })
            .filter(Objects::nonNull)
            .toList();
    }

    /**
     * 按绑定 skillIds 加载 ACTIVE Skill 短字段（不含手册 / 契约）。
     *
     * <p>CUSTOM 隔离查 + PLATFORM 不隔离查，合并为 id → PO。
     */
    private Map<Long, SkillProfilePo> loadActiveDigestSkills(List<AgentSkillBindingPo> skillBindings) {
        Set<Long> skillIds = skillBindings.stream()
            .map(AgentSkillBindingPo::getSkillId)
            .filter(Objects::nonNull)
            .collect(Collectors.toCollection(HashSet::new));
        if (Collections.isEmpty(skillIds)) {
            return Map.of();
        }

        SkillProfileSelectDto custom = new SkillProfileSelectDto();
        custom.setIds(skillIds);
        custom.setResourceType(ResourceType.CUSTOM.name());
        custom.setStatus(ResourceStatus.ACTIVE.name());
        SkillProfileSelectDto platform = new SkillProfileSelectDto();
        platform.setIds(skillIds);
        platform.setResourceType(ResourceType.PLATFORM.name());
        platform.setStatus(ResourceStatus.ACTIVE.name());
        return Stream.concat(
            skillProfileDao.selectDigestList(custom).stream(),
            skillProfileDao.selectDigestListWithoutIsolation(platform).stream()
        ).collect(Collectors.toMap(SkillProfilePo::getId, Function.identity(), (a, _) -> a));
    }

    /**
     * 按 agentId 查询能力包：Skill（手册/契约）+ 其下 Tool 列表。
     *
     * <p>绑定 → ACTIVE Skill 全量字段 → 按 skillIds 拉 Tool，挂到对应 Skill。
     *
     * @param agentId 数字员工标识
     * @return 能力包；无绑定时 skills 为空列表
     */
    @Override
    public AgentBundleVo selectCapabilityBundle(Long agentId) {
        AgentSkillBindingSelectDto bindingSelect = new AgentSkillBindingSelectDto();
        bindingSelect.setAgentId(agentId);
        bindingSelect.setEnabledFlag(EnabledFlag.YES.getCode());
        List<AgentSkillBindingPo> bindings = agentSkillBindingDao.selectList(bindingSelect);
        if (Collections.isEmpty(bindings)) {
            return new AgentBundleVo(List.of());
        }

        Map<Long, SkillProfilePo> skillMap = loadActiveSkills(bindings);

        Set<Long> skillIds = skillMap.keySet();
        SkillToolBindingSelectDto toolSelect = new SkillToolBindingSelectDto();
        toolSelect.setSkillIds(skillIds);
        toolSelect.setEnabledFlag(EnabledFlag.YES.getCode());
        List<SkillToolBindingPo> toolBindings = skillToolBindingDao.selectList(toolSelect);

        Map<Long, List<AgentBundleVo.ToolBriefVo>> toolsBySkill = toolBindings.stream()
            .collect(Collectors.groupingBy(
                SkillToolBindingPo::getSkillId,
                Collectors.mapping(b -> {
                    AgentBundleVo.ToolBriefVo t = new AgentBundleVo.ToolBriefVo();
                    t.setToolName(b.getToolName());
                    return t;
                }, Collectors.toList())
            ));

        List<AgentBundleVo.SkillBriefVo> skills = bindings.stream()
            .map(binding -> {
                SkillProfilePo skill = skillMap.get(binding.getSkillId());
                if (skill == null) {
                    return null;
                }
                AgentBundleVo.SkillBriefVo brief = new AgentBundleVo.SkillBriefVo();
                brief.setSkillKey(skill.getSkillKey());
                brief.setSkillName(skill.getSkillName());
                brief.setSemanticDesc(skill.getSemanticDesc());
                brief.setSkillManual(skill.getSkillManual());
                brief.setSkillContract(skill.getSkillContract());
                brief.setTools(toolsBySkill.getOrDefault(binding.getSkillId(), List.of()));
                return brief;
            })
            .filter(Objects::nonNull)
            .toList();

        return new AgentBundleVo(skills);
    }

    /**
     * 按绑定 skillIds 加载 ACTIVE Skill 完整字段（含手册 / 契约）。
     *
     * <p>CUSTOM 隔离查 + PLATFORM 不隔离查，合并为 id → PO。
     */
    private Map<Long, SkillProfilePo> loadActiveSkills(List<AgentSkillBindingPo> skillBindings) {
        Set<Long> skillIds = skillBindings.stream()
            .map(AgentSkillBindingPo::getSkillId)
            .filter(Objects::nonNull)
            .collect(Collectors.toCollection(HashSet::new));
        if (Collections.isEmpty(skillIds)) {
            return Map.of();
        }

        SkillProfileSelectDto custom = new SkillProfileSelectDto();
        custom.setIds(skillIds);
        custom.setResourceType(ResourceType.CUSTOM.name());
        custom.setStatus(ResourceStatus.ACTIVE.name());
        SkillProfileSelectDto platform = new SkillProfileSelectDto();
        platform.setIds(skillIds);
        platform.setResourceType(ResourceType.PLATFORM.name());
        platform.setStatus(ResourceStatus.ACTIVE.name());
        return Stream.concat(
            skillProfileDao.selectList(custom).stream(),
            skillProfileDao.selectListWithoutIsolation(platform).stream()
        ).collect(Collectors.toMap(SkillProfilePo::getId, Function.identity(), (a, _) -> a));
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
    public Long save(AgentProfileDto dto) {
        Validations.validateSave(dto);

        // 转换 DTO 为 PO
        AgentProfilePo entity = AgentProfileConverter.INSTANCE.dto2po(dto);

        // 判断是新增还是更新
        Long id = entity.getId();
        if (Objects.isNull(id) || id == 0) {
            // 新增：使用IdGenerator生成主键
            entity.setId(idGenerator.generateId());
            LocalDateTime now = Moments.now();
            entity.setUpdateTime(now);
            entity.setCreateTime(now);
            int success = agentProfileDao.insert(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.CREATE_DATA_ERROR);
        } else {
            AgentProfilePo existing = agentProfileDao.selectById(id);
            Asserts.isTrue(Objects.nonNull(existing), SystemErrorCode.DATA_NOT_EXISTS, id);
            // 更新：直接更新
            entity.setUpdateTime(Moments.now());
            int success = agentProfileDao.update(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.UPDATE_DATA_ERROR, id);
        }

        return entity.getId();
    }

    @Override
    public int delete(Long id) {
        AgentProfilePo existing = agentProfileDao.selectById(id);
        Asserts.isTrue(Objects.nonNull(existing), SystemErrorCode.DATA_NOT_EXISTS, id);
        return agentProfileDao.delete(id);
    }
}
