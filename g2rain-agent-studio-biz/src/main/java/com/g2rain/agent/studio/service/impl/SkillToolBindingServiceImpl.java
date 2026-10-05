package com.g2rain.agent.studio.service.impl;

import com.g2rain.agent.studio.converter.SkillToolBindingConverter;
import com.g2rain.agent.studio.dao.SkillToolBindingDao;
import com.g2rain.agent.studio.dao.po.SkillToolBindingPo;
import com.g2rain.agent.studio.dto.SkillToolBindingDto;
import com.g2rain.agent.studio.dto.SkillToolBindingSelectDto;
import com.g2rain.agent.studio.service.SkillToolBindingService;
import com.g2rain.agent.studio.vo.SkillToolBindingVo;
import com.g2rain.common.exception.SystemErrorCode;
import com.g2rain.common.id.IdGenerator;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.utils.Asserts;
import com.g2rain.common.utils.Moments;
import com.g2rain.common.validation.Validations;
import com.g2rain.mybatis.pagination.PageContext;
import com.g2rain.mybatis.pagination.model.Page;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Skill Tool 绑定表服务实现类
 * 表名: skill_tool_binding
 *
 * @author G2rain Generator
 */
@Service(value = "skillToolBindingServiceImpl")
public class SkillToolBindingServiceImpl implements SkillToolBindingService {

    @Resource(name = "skillToolBindingDao")
    private SkillToolBindingDao skillToolBindingDao;

    private IdGenerator idGenerator;

    @Qualifier("idGenerator")
    @Autowired(required = false)
    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public List<SkillToolBindingVo> selectList(SkillToolBindingSelectDto selectDto) {
        return skillToolBindingDao.selectList(selectDto)
            .stream()
            .map(SkillToolBindingConverter.INSTANCE::po2vo)
            .toList();
    }

    @Override
    public PageData<SkillToolBindingVo> selectPage(PageSelectListDto<SkillToolBindingSelectDto> selectDto) {
        Page<SkillToolBindingPo> page = PageContext.of(selectDto.getPageNum(), selectDto.getPageSize(), () -> {
            skillToolBindingDao.selectList(selectDto.getQuery());
        });
        List<SkillToolBindingVo> result = page.getResult()
            .stream()
            .map(SkillToolBindingConverter.INSTANCE::po2vo)
            .toList();
        return PageData.of(page.getPageNum(), page.getPageSize(), page.getTotal(), result);
    }

    @Override
    public Long save(SkillToolBindingDto dto) {
        Validations.validateSave(dto);

        // 转换 DTO 为 PO
        SkillToolBindingPo entity = SkillToolBindingConverter.INSTANCE.dto2po(dto);

        // 判断是新增还是更新
        Long id = entity.getId();
        if (Objects.isNull(id) || id == 0) {
            // 新增：使用IdGenerator生成主键
            entity.setId(idGenerator.generateId());
            LocalDateTime now = Moments.now();
            entity.setUpdateTime(now);
            entity.setCreateTime(now);
            int success = skillToolBindingDao.insert(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.CREATE_DATA_ERROR);
        } else {
            SkillToolBindingPo existing = skillToolBindingDao.selectById(id);
            Asserts.isTrue(Objects.nonNull(existing), SystemErrorCode.DATA_NOT_EXISTS, id);
            // 更新：直接更新
            entity.setUpdateTime(Moments.now());
            int success = skillToolBindingDao.update(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.UPDATE_DATA_ERROR, id);
        }

        return entity.getId();
    }

    @Override
    public int delete(Long id) {
        SkillToolBindingPo existing = skillToolBindingDao.selectById(id);
        Asserts.isTrue(Objects.nonNull(existing), SystemErrorCode.DATA_NOT_EXISTS, id);
        return skillToolBindingDao.delete(id);
    }
}
