package com.g2rain.agent.studio.service;

import com.g2rain.agent.studio.dto.SkillProfileDto;
import com.g2rain.agent.studio.dto.SkillProfileSelectDto;
import com.g2rain.agent.studio.dto.UpdateStatusDto;
import com.g2rain.agent.studio.vo.SkillProfileVo;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;

import java.util.List;

/**
 * Skill 定义表服务接口
 * 表名: skill_profile
 *
 * @author G2rain Generator
 */
public interface SkillProfileService {

    /**
     * 根据条件查询列表
     *
     * @param selectDto 查询条件 DTO
     * @return VO 对象列表
     */
    List<SkillProfileVo> selectList(SkillProfileSelectDto selectDto);

    /**
     * 根据条件分页查询
     *
     * @param selectDto 查询条件DTO（包含分页参数）
     * @return 分页 VO 数据
     */
    PageData<SkillProfileVo> selectPage(PageSelectListDto<SkillProfileSelectDto> selectDto);

    /**
     * 新增或更新数据
     *
     * @param dto 数据传输对象
     * @return 操作结果（影响行数）
     */
    Long save(SkillProfileDto dto);

    /**
     * 根据 ID 删除数据
     *
     * @param id 主键 ID
     * @return 操作结果（影响行数）
     */
    int delete(Long id);

    /**
     * 仅修改状态。
     *
     * @param id  主键
     * @param dto 状态
     * @return 影响行数
     */
    int updateStatus(Long id, UpdateStatusDto dto);
}
