package com.g2rain.agent.studio.service;

import com.g2rain.agent.studio.dto.ChatPromptTemplateDto;
import com.g2rain.agent.studio.dto.ChatPromptTemplateSelectDto;
import com.g2rain.agent.studio.dto.UpdateStatusDto;
import com.g2rain.agent.studio.vo.ChatPromptTemplateVo;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;

import java.util.List;

/**
 * Chat 问题模板服务接口
 * 表名: chat_prompt_template
 *
 * @author G2rain Generator
 */
public interface ChatPromptTemplateService {

    /**
     * 根据条件查询列表
     *
     * @param selectDto 查询条件 DTO
     * @return VO 对象列表
     */
    List<ChatPromptTemplateVo> selectList(ChatPromptTemplateSelectDto selectDto);

    /**
     * 根据条件分页查询
     *
     * @param selectDto 查询条件DTO（包含分页参数）
     * @return 分页 VO 数据
     */
    PageData<ChatPromptTemplateVo> selectPage(PageSelectListDto<ChatPromptTemplateSelectDto> selectDto);

    /**
     * 新增或更新数据
     *
     * @param dto 数据传输对象
     * @return 操作结果（影响行数）
     */
    Long save(ChatPromptTemplateDto dto);

    /**
     * 修改问题模板状态。
     *
     * @param id 问题模板 ID
     * @param dto 状态修改参数，仅允许 ACTIVE 或 INACTIVE
     * @return 数据库更新的记录数
     */
    int updateStatus(Long id, UpdateStatusDto dto);

    /**
     * 根据 ID 删除数据
     *
     * @param id 主键 ID
     * @return 操作结果（影响行数）
     */
    int delete(Long id);
}
