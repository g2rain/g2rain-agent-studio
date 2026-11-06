package com.g2rain.agent.studio.service.impl;

import com.g2rain.common.exception.SystemErrorCode;
import com.g2rain.common.id.IdGenerator;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.utils.Asserts;
import com.g2rain.common.utils.Moments;
import com.g2rain.common.validation.Validations;
import com.g2rain.agent.studio.converter.AgentSessionBindingConverter;
import com.g2rain.agent.studio.dao.AgentProfileDao;
import com.g2rain.agent.studio.dao.AgentSessionBindingDao;
import com.g2rain.agent.studio.dao.po.AgentProfilePo;
import com.g2rain.agent.studio.dao.po.AgentSessionBindingPo;
import com.g2rain.agent.studio.dto.AgentSessionAssignDto;
import com.g2rain.agent.studio.dto.AgentSessionBindingDto;
import com.g2rain.agent.studio.dto.AgentSessionBindingSelectDto;
import com.g2rain.agent.studio.service.AgentSessionBindingService;
import com.g2rain.agent.studio.utils.OwnerGuard;
import com.g2rain.agent.studio.vo.AgentSessionBindingVo;
import com.g2rain.common.utils.Collections;
import com.g2rain.mybatis.pagination.PageContext;
import com.g2rain.mybatis.pagination.model.Page;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 智能体会话绑定表服务实现类
 * 表名: agent_session_binding
 *
 * @author G2rain Generator
 */
@Service(value = "agentSessionBindingServiceImpl")
public class AgentSessionBindingServiceImpl implements AgentSessionBindingService {

    @Resource(name = "agentSessionBindingDao")
    private AgentSessionBindingDao agentSessionBindingDao;

    @Resource(name = "agentProfileDao")
    private AgentProfileDao agentProfileDao;

    private IdGenerator idGenerator;

    @Qualifier("idGenerator")
    @Autowired(required = false)
    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public List<AgentSessionBindingVo> selectList(AgentSessionBindingSelectDto selectDto) {
        return agentSessionBindingDao.selectList(selectDto)
            .stream()
            .map(AgentSessionBindingConverter.INSTANCE::po2vo)
            .toList();
    }

    @Override
    public PageData<AgentSessionBindingVo> selectPage(PageSelectListDto<AgentSessionBindingSelectDto> selectDto) {
        Page<AgentSessionBindingPo> page = PageContext.of(selectDto.getPageNum(), selectDto.getPageSize(), () -> {
            agentSessionBindingDao.selectList(selectDto.getQuery());
        });
        List<AgentSessionBindingVo> result = page.getResult()
            .stream()
            .map(AgentSessionBindingConverter.INSTANCE::po2vo)
            .toList();
        return PageData.of(page.getPageNum(), page.getPageSize(), page.getTotal(), result);
    }

    @Override
    public Long save(AgentSessionBindingDto dto) {
        Validations.validateSave(dto);

        // 转换 DTO 为 PO
        AgentSessionBindingPo entity = AgentSessionBindingConverter.INSTANCE.dto2po(dto);

        // 判断是新增还是更新
        Long id = entity.getId();
        if (Objects.isNull(id) || id == 0) {
            // 新增：使用IdGenerator生成主键
            entity.setId(idGenerator.generateId());
            LocalDateTime now = Moments.now();
            entity.setUpdateTime(now);
            entity.setCreateTime(now);
            int success = agentSessionBindingDao.insert(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.CREATE_DATA_ERROR);
        } else {
            AgentSessionBindingPo existing = agentSessionBindingDao.selectById(id);
            Asserts.isTrue(Objects.nonNull(existing), SystemErrorCode.DATA_NOT_EXISTS, id);
            // 更新：直接更新
            entity.setUpdateTime(Moments.now());
            int success = agentSessionBindingDao.update(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.UPDATE_DATA_ERROR, id);
        }

        return entity.getId();
    }

    @Override
    public int delete(Long id) {
        AgentSessionBindingPo existing = agentSessionBindingDao.selectById(id);
        Asserts.isTrue(Objects.nonNull(existing), SystemErrorCode.DATA_NOT_EXISTS, id);
        return agentSessionBindingDao.delete(id);
    }

    /**
     * 访问授权：校验智能体归属后，按提交集合与现有绑定做差异同步（新增插入、取消逻辑删除）。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int assign(AgentSessionAssignDto dto) {
        Long agentId = dto.getAgentId();
        AgentProfilePo agent = agentProfileDao.selectById(agentId);
        Asserts.isTrue(Objects.nonNull(agent), SystemErrorCode.DATA_NOT_EXISTS, agentId);
        OwnerGuard.requireOwner(agent.getOrganId());

        AgentSessionBindingSelectDto select = new AgentSessionBindingSelectDto();
        select.setAgentId(agentId);
        Map<String, Long> existing = agentSessionBindingDao.selectList(select).stream()
            .collect(Collectors.toMap(AgentSessionBindingPo::getSessionType, AgentSessionBindingPo::getId));
        Set<String> targets = dto.getSessionTypes().stream()
            .map(Enum::name)
            .collect(Collectors.toSet());

        int result = 0;
        for (Map.Entry<String, Long> entry : existing.entrySet()) {
            if (!targets.contains(entry.getKey())) {
                result += agentSessionBindingDao.delete(entry.getValue());
            }
        }

        LocalDateTime now = Moments.now();
        List<AgentSessionBindingPo> added = targets.stream()
            .filter(sessionType -> !existing.containsKey(sessionType))
            .map(sessionType -> {
                AgentSessionBindingPo binding = new AgentSessionBindingPo();
                binding.setId(idGenerator.generateId());
                binding.setOrganId(agent.getOrganId());
                binding.setAgentId(agentId);
                binding.setSessionType(sessionType);
                binding.setCreateTime(now);
                binding.setUpdateTime(now);
                return binding;
            })
            .toList();
        if (Collections.isNotEmpty(added)) {
            result += agentSessionBindingDao.insertMultiple(added);
        }

        return result;
    }
}
