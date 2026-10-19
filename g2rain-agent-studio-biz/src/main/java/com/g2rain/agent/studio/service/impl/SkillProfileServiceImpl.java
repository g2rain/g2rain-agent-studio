package com.g2rain.agent.studio.service.impl;

import com.g2rain.agent.studio.converter.SkillProfileConverter;
import com.g2rain.agent.studio.dao.AgentProfileDao;
import com.g2rain.agent.studio.dao.SkillProfileDao;
import com.g2rain.agent.studio.dao.SkillToolBindingDao;
import com.g2rain.agent.studio.dao.po.AgentProfilePo;
import com.g2rain.agent.studio.dao.po.SkillProfilePo;
import com.g2rain.agent.studio.dto.SkillProfileDto;
import com.g2rain.agent.studio.dto.SkillProfileSelectDto;
import com.g2rain.agent.studio.dto.SkillToolBindingSelectDto;
import com.g2rain.agent.studio.dto.UpdateStatusDto;
import com.g2rain.agent.studio.enums.ResourceStatus;
import com.g2rain.agent.studio.service.SkillProfileService;
import com.g2rain.agent.studio.vo.SkillProfileVo;
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
import java.util.List;
import java.util.Objects;

/**
 * Skill 定义表服务实现类
 * 表名: skill_profile
 *
 * @author G2rain Generator
 */
@Service(value = "skillProfileServiceImpl")
public class SkillProfileServiceImpl implements SkillProfileService {

    @Resource(name = "skillProfileDao")
    private SkillProfileDao skillProfileDao;

    @Resource(name = "agentProfileDao")
    private AgentProfileDao agentProfileDao;

    @Resource(name = "skillToolBindingDao")
    private SkillToolBindingDao skillToolBindingDao;

    private IdGenerator idGenerator;

    @Qualifier("idGenerator")
    @Autowired(required = false)
    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public List<SkillProfileVo> selectList(SkillProfileSelectDto selectDto) {
        return skillProfileDao.selectList(selectDto)
            .stream()
            .map(SkillProfileConverter.INSTANCE::po2vo)
            .toList();
    }

    @Override
    public PageData<SkillProfileVo> selectPage(PageSelectListDto<SkillProfileSelectDto> selectDto) {
        Page<SkillProfilePo> page = PageContext.of(selectDto.getPageNum(), selectDto.getPageSize(), () -> {
            skillProfileDao.selectList(selectDto.getQuery());
        });
        List<SkillProfileVo> result = page.getResult()
            .stream()
            .map(SkillProfileConverter.INSTANCE::po2vo)
            .toList();
        return PageData.of(page.getPageNum(), page.getPageSize(), page.getTotal(), result);
    }

    @Override
    public Long save(SkillProfileDto dto) {
        Validations.validateSave(dto);
        // resourceType 跟所属智能体走，不信客户端传值
        AgentProfilePo agent = agentProfileDao.selectById(dto.getAgentId());
        Asserts.isTrue(Objects.nonNull(agent), SystemErrorCode.DATA_NOT_EXISTS, dto.getAgentId());
        dto.setResourceType(agent.getResourceType());

        // 转换 DTO 为 PO
        SkillProfilePo entity = SkillProfileConverter.INSTANCE.dto2po(dto);

        // 判断是新增还是更新
        Long id = entity.getId();
        if (Objects.isNull(id) || id == 0) {
            // 同一智能体下 skillKey 唯一
            SkillProfileSelectDto keySelect = new SkillProfileSelectDto();
            keySelect.setAgentId(dto.getAgentId());
            keySelect.setSkillKey(dto.getSkillKey());
            Asserts.isTrue(Collections.isEmpty(skillProfileDao.selectListWithoutIsolation(keySelect)),
                SystemErrorCode.DATA_EXISTS, dto.getSkillKey());

            // 新增：使用IdGenerator生成主键；状态固定 ACTIVE（启停走 updateStatus）
            entity.setId(idGenerator.generateId());
            entity.setStatus(ResourceStatus.ACTIVE.name());
            LocalDateTime now = Moments.now();
            entity.setUpdateTime(now);
            entity.setCreateTime(now);
            int success = skillProfileDao.insert(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.CREATE_DATA_ERROR);
        } else {
            SkillProfilePo existing = skillProfileDao.selectById(id);
            Asserts.isTrue(Objects.nonNull(existing), SystemErrorCode.DATA_NOT_EXISTS, id);
            // 更新：不允许改状态、技能键（启停 / 改键另走）
            entity.setStatus(null);
            entity.setSkillKey(null);
            entity.setUpdateTime(Moments.now());
            Asserts.greaterThan(skillProfileDao.update(entity), 0, SystemErrorCode.UPDATE_DATA_ERROR, id);
        }

        return entity.getId();
    }

    /**
     * 删除 Skill：级联删除其下 Tool 绑定。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long id) {
        SkillProfilePo existing = skillProfileDao.selectById(id);
        Asserts.isTrue(Objects.nonNull(existing), SystemErrorCode.DATA_NOT_EXISTS, id);

        SkillToolBindingSelectDto toolSelect = new SkillToolBindingSelectDto();
        toolSelect.setSkillId(id);
        skillToolBindingDao.selectList(toolSelect).forEach(b -> skillToolBindingDao.delete(b.getId()));
        return skillProfileDao.delete(id);
    }

    @Override
    public int updateStatus(Long id, UpdateStatusDto dto) {
        ResourceStatus.validate(dto.getStatus());

        SkillProfilePo existing = skillProfileDao.selectById(id);
        Asserts.isTrue(Objects.nonNull(existing), SystemErrorCode.DATA_NOT_EXISTS, id);

        SkillProfilePo entity = new SkillProfilePo();
        entity.setId(id);
        entity.setStatus(dto.getStatus());
        entity.setUpdateTime(Moments.now());
        int success = skillProfileDao.update(entity);
        Asserts.greaterThan(success, 0, SystemErrorCode.UPDATE_DATA_ERROR, id);
        return success;
    }
}
