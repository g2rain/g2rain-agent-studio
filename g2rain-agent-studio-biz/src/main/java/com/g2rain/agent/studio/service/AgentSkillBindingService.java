package com.g2rain.agent.studio.service;

import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.agent.studio.dto.AgentSkillBindingDto;
import com.g2rain.agent.studio.dto.AgentSkillBindingSelectDto;
import com.g2rain.agent.studio.vo.AgentSkillBindingVo;

import java.util.List;

/**
 * 数字员工 Skill 绑定表服务接口
 * 表名: agent_skill_binding
 *
 * @author G2rain Generator
 */
public interface AgentSkillBindingService {

    /**
     * 根据条件查询列表
     *
     * @param selectDto 查询条件 DTO
     * @return VO 对象列表
     */
    List<AgentSkillBindingVo> selectList(AgentSkillBindingSelectDto selectDto);

    /**
     * 根据条件分页查询
     *
     * @param selectDto 查询条件DTO（包含分页参数）
     * @return 分页 VO 数据
     */
    PageData<AgentSkillBindingVo> selectPage(PageSelectListDto<AgentSkillBindingSelectDto> selectDto);

    /**
     * 新增或更新数据
     *
     * @param dto 数据传输对象
     * @return 操作结果（影响行数）
     */
    Long save(AgentSkillBindingDto dto);

    /**
     * 根据 ID 删除数据
     *
     * @param id 主键 ID
     * @return 操作结果（影响行数）
     */
    int delete(Long id);
}