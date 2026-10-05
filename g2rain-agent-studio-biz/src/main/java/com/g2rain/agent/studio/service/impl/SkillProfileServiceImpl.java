package com.g2rain.agent.studio.service.impl;

import com.g2rain.agent.studio.converter.SkillProfileConverter;
import com.g2rain.agent.studio.dao.SkillProfileDao;
import com.g2rain.agent.studio.dao.po.SkillProfilePo;
import com.g2rain.agent.studio.dto.SkillProfileDto;
import com.g2rain.agent.studio.dto.SkillProfileSelectDto;
import com.g2rain.agent.studio.service.SkillProfileService;
import com.g2rain.agent.studio.vo.SkillProfileVo;
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
 * Skill 定义表服务实现类
 * 表名: skill_profile
 *
 * @author G2rain Generator
 */
@Service(value = "skillProfileServiceImpl")
public class SkillProfileServiceImpl implements SkillProfileService {

    @Resource(name = "skillProfileDao")
    private SkillProfileDao skillProfileDao;

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

        // 转换 DTO 为 PO
        SkillProfilePo entity = SkillProfileConverter.INSTANCE.dto2po(dto);

        // 判断是新增还是更新
        Long id = entity.getId();
        if (Objects.isNull(id) || id == 0) {
            // 新增：使用IdGenerator生成主键
            entity.setId(idGenerator.generateId());
            LocalDateTime now = Moments.now();
            entity.setUpdateTime(now);
            entity.setCreateTime(now);
            int success = skillProfileDao.insert(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.CREATE_DATA_ERROR);
        } else {
            SkillProfilePo existing = skillProfileDao.selectById(id);
            Asserts.isTrue(Objects.nonNull(existing), SystemErrorCode.DATA_NOT_EXISTS, id);
            // 更新：直接更新
            entity.setUpdateTime(Moments.now());
            int success = skillProfileDao.update(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.UPDATE_DATA_ERROR, id);
        }

        return entity.getId();
    }

    @Override
    public int delete(Long id) {
        SkillProfilePo existing = skillProfileDao.selectById(id);
        Asserts.isTrue(Objects.nonNull(existing), SystemErrorCode.DATA_NOT_EXISTS, id);
        return skillProfileDao.delete(id);
    }
}
