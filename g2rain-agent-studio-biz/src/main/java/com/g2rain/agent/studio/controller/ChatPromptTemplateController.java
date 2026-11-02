package com.g2rain.agent.studio.controller;

import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.model.Result;
import com.g2rain.agent.studio.api.ChatPromptTemplateApi;
import com.g2rain.agent.studio.dto.ChatPromptTemplateDto;
import com.g2rain.agent.studio.dto.ChatPromptTemplateSelectDto;
import com.g2rain.agent.studio.dto.UpdateStatusDto;
import com.g2rain.agent.studio.service.ChatPromptTemplateService;
import com.g2rain.agent.studio.vo.ChatPromptTemplateVo;
import com.g2rain.web.interceptors.annotations.IdentityInject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Chat 问题模板控制器
 * 表名: chat_prompt_template
 *
 * @author G2rain Generator
 */
@RestController
@RequestMapping("/chat_prompt_template")
public class ChatPromptTemplateController implements ChatPromptTemplateApi {

    @Resource(name = "chatPromptTemplateServiceImpl")
    private ChatPromptTemplateService chatPromptTemplateService;

    @Override
    @IdentityInject(organIdPropertyName = "organId")
    public Result<List<ChatPromptTemplateVo>> selectList(ChatPromptTemplateSelectDto selectDto) {
        return Result.success(chatPromptTemplateService.selectList(selectDto));
    }

    @Override
    @IdentityInject(organIdPropertyName = "organId")
    public Result<PageData<ChatPromptTemplateVo>> selectPage(PageSelectListDto<ChatPromptTemplateSelectDto> selectDto) {
        return Result.successPage(chatPromptTemplateService.selectPage(selectDto));
    }

    @PostMapping("/save")
    @IdentityInject(organIdPropertyName = "organId")
    @Operation(summary = "新增或更新Chat 问题模板信息", description = "新增或更新Chat 问题模板基础信息")
    public Result<Long> save(@RequestBody ChatPromptTemplateDto dto) {
        return Result.success(chatPromptTemplateService.save(dto));
    }

    @PostMapping("/{id}/status")
    @Operation(summary = "修改问题模板状态", description = "仅允许修改问题模板状态")
    public Result<Integer> updateStatus(@Parameter(description = "问题模板标识") @PathVariable Long id, @RequestBody @Validated UpdateStatusDto dto) {
        return Result.success(chatPromptTemplateService.updateStatus(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除Chat 问题模板记录", description = "根据主键删除Chat 问题模板记录")
    public Result<Integer> delete(@Parameter(description = "Chat 问题模板标识") @PathVariable Long id) {
        return Result.success(chatPromptTemplateService.delete(id));
    }
}
