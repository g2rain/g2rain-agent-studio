package com.g2rain.agent.studio.api;

import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.model.Result;
import com.g2rain.agent.studio.dto.ChatPromptTemplateSelectDto;
import com.g2rain.agent.studio.vo.ChatPromptTemplateVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


/**
 * Chat 问题模板API接口
 * 表名: chat_prompt_template
 *
 * @author G2rain Generator
 */
@Tag(name = "Chat 问题模板", description = "Chat 问题模板相关接口")
public interface ChatPromptTemplateApi {

    /**
     * 根据条件查询列表
     *
     * @param selectDto 查询条件 DTO
     * @return 数据列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询Chat 问题模板列表", description = "根据查询条件返回Chat 问题模板列表")
    Result<List<ChatPromptTemplateVo>> selectList(ChatPromptTemplateSelectDto selectDto);

    /**
     * 根据条件分页查询
     *
     * @param selectDto 查询条件DTO（包含分页参数）
     * @return 分页数据
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询Chat 问题模板列表", description = "分页查询Chat 问题模板列表")
    Result<PageData<ChatPromptTemplateVo>> selectPage(PageSelectListDto<ChatPromptTemplateSelectDto> selectDto);
}