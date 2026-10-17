package com.g2rain.agent.studio.controller;

import com.g2rain.agent.studio.api.SkillToolBindingApi;
import com.g2rain.agent.studio.dto.SkillToolBindingDto;
import com.g2rain.agent.studio.dto.SkillToolBindingSelectDto;
import com.g2rain.agent.studio.dto.UpdateEnabledFlagDto;
import com.g2rain.agent.studio.service.SkillToolBindingService;
import com.g2rain.agent.studio.vo.SkillToolBindingVo;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.model.Result;
import com.g2rain.web.interceptors.annotations.IdentityInject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Skill Tool 绑定表控制器
 * 表名: skill_tool_binding
 *
 * @author G2rain Generator
 */
@RestController
@RequestMapping("/skill_tool_binding")
public class SkillToolBindingController implements SkillToolBindingApi {

    @Resource(name = "skillToolBindingServiceImpl")
    private SkillToolBindingService skillToolBindingService;

    @Override
    public Result<List<SkillToolBindingVo>> selectList(SkillToolBindingSelectDto selectDto) {
        return Result.success(skillToolBindingService.selectList(selectDto));
    }

    @Override
    public Result<PageData<SkillToolBindingVo>> selectPage(PageSelectListDto<SkillToolBindingSelectDto> selectDto) {
        return Result.successPage(skillToolBindingService.selectPage(selectDto));
    }

    @PostMapping("/save")
    @IdentityInject(organIdPropertyName = "organId")
    @Operation(summary = "新增或更新Skill Tool 绑定表信息", description = "新增固定启用；更新不改 enabledFlag，启停走 /{id}/enabled")
    public Result<Long> save(@RequestBody SkillToolBindingDto dto) {
        return Result.success(skillToolBindingService.save(dto));
    }

    @PostMapping("/{id}/enabled")
    @Operation(summary = "修改工具绑定启用标记", description = "仅允许改 enabledFlag")
    public Result<Integer> updateEnabled(@Parameter(description = "绑定标识") @PathVariable Long id,
                                         @RequestBody @Validated UpdateEnabledFlagDto dto) {
        return Result.success(skillToolBindingService.updateEnabled(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除Skill Tool 绑定表记录", description = "根据主键删除Skill Tool 绑定表记录")
    public Result<Integer> delete(@Parameter(description = "Skill Tool 绑定表标识") @PathVariable Long id) {
        return Result.success(skillToolBindingService.delete(id));
    }
}
