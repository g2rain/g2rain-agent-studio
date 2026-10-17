package com.g2rain.agent.studio.service;

import com.g2rain.agent.studio.dto.SkillToolBindingDto;
import com.g2rain.agent.studio.dto.SkillToolBindingSelectDto;
import com.g2rain.agent.studio.dto.UpdateEnabledFlagDto;
import com.g2rain.agent.studio.vo.SkillToolBindingVo;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;

import java.util.List;

/**
 * Skill Tool 绑定表服务接口
 * 表名: skill_tool_binding
 *
 * @author G2rain Generator
 */
public interface SkillToolBindingService {

    /**
     * 根据条件查询列表
     *
     * @param selectDto 查询条件 DTO
     * @return VO 对象列表
     */
    List<SkillToolBindingVo> selectList(SkillToolBindingSelectDto selectDto);

    /**
     * 根据条件分页查询
     *
     * @param selectDto 查询条件DTO（包含分页参数）
     * @return 分页 VO 数据
     */
    PageData<SkillToolBindingVo> selectPage(PageSelectListDto<SkillToolBindingSelectDto> selectDto);

    /**
     * 新增或更新数据
     *
     * @param dto 数据传输对象
     * @return 操作结果（影响行数）
     */
    Long save(SkillToolBindingDto dto);

    /**
     * 根据 ID 删除数据
     *
     * @param id 主键 ID
     * @return 操作结果（影响行数）
     */
    int delete(Long id);

    /**
     * 仅修改启用标记。
     *
     * @param id  主键
     * @param dto 启用标记
     * @return 影响行数
     */
    int updateEnabled(Long id, UpdateEnabledFlagDto dto);
}
