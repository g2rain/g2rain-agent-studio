package com.g2rain.agent.studio.controller;

import com.g2rain.agent.studio.api.SkillProfileApi;
import com.g2rain.agent.studio.dto.SkillProfileDto;
import com.g2rain.agent.studio.dto.SkillProfileSelectDto;
import com.g2rain.agent.studio.dto.UpdateStatusDto;
import com.g2rain.agent.studio.service.SkillProfileService;
import com.g2rain.agent.studio.vo.SkillProfileVo;
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
 * Skill 定义表控制器
 * 表名: skill_profile
 *
 * @author G2rain Generator
 */
@RestController
@RequestMapping("/skill_profile")
public class SkillProfileController implements SkillProfileApi {

    @Resource(name = "skillProfileServiceImpl")
    private SkillProfileService skillProfileService;

    @Override
    public Result<List<SkillProfileVo>> selectList(SkillProfileSelectDto selectDto) {
        return Result.success(skillProfileService.selectList(selectDto));
    }

    @Override
    public Result<PageData<SkillProfileVo>> selectPage(PageSelectListDto<SkillProfileSelectDto> selectDto) {
        return Result.successPage(skillProfileService.selectPage(selectDto));
    }

    @PostMapping("/save")
    @IdentityInject(organIdPropertyName = "organId")
    @Operation(summary = "新增或更新Skill 定义表信息", description = "新增或更新Skill 定义表基础信息")
    public Result<Long> save(@RequestBody SkillProfileDto dto) {
        return Result.success(skillProfileService.save(dto));
    }

    @PostMapping("/{id}/status")
    @Operation(summary = "修改技能状态", description = "仅允许改状态")
    public Result<Integer> updateStatus(@Parameter(description = "技能标识") @PathVariable Long id,
                                        @RequestBody @Validated UpdateStatusDto dto) {
        return Result.success(skillProfileService.updateStatus(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除Skill 定义表记录", description = "根据主键删除Skill 定义表记录")
    public Result<Integer> delete(@Parameter(description = "Skill 定义表标识") @PathVariable Long id) {
        return Result.success(skillProfileService.delete(id));
    }
}
