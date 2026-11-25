package com.g2rain.agent.studio.controller;

import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.model.Result;
import com.g2rain.agent.studio.api.SkillRevisionApi;
import com.g2rain.agent.studio.dto.CreateSkillRevisionDraftDto;
import com.g2rain.agent.studio.dto.PublishSkillRevisionDto;
import com.g2rain.agent.studio.dto.SkillRevisionDto;
import com.g2rain.agent.studio.dto.SkillRevisionSelectDto;
import com.g2rain.agent.studio.service.SkillRevisionService;
import com.g2rain.agent.studio.vo.SkillRevisionVo;
import com.g2rain.web.interceptors.annotations.IdentityInject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Skill 修订表控制器
 * 表名: skill_revision
 *
 * @author G2rain Generator
 */
@RestController
@RequestMapping("/skill_revision")
public class SkillRevisionController implements SkillRevisionApi {

    @Resource(name = "skillRevisionServiceImpl")
    private SkillRevisionService skillRevisionService;

    @Override
    @IdentityInject(organIdPropertyName = "organId")
    public Result<List<SkillRevisionVo>> selectList(SkillRevisionSelectDto selectDto) {
        return Result.success(skillRevisionService.selectList(selectDto));
    }

    @Override
    @IdentityInject(organIdPropertyName = "organId")
    public Result<PageData<SkillRevisionVo>> selectPage(PageSelectListDto<SkillRevisionSelectDto> selectDto) {
        return Result.successPage(skillRevisionService.selectPage(selectDto));
    }

    @Override
    public Result<SkillRevisionVo> selectById(Long id) {
        return Result.success(skillRevisionService.selectById(id));
    }

    @Override
    @IdentityInject(organIdPropertyName = "organId")
    public Result<Long> createDraft(CreateSkillRevisionDraftDto dto) {
        return Result.success(skillRevisionService.createDraft(dto));
    }

    @Override
    public Result<Void> publish(Long id, PublishSkillRevisionDto dto) {
        skillRevisionService.publish(id, dto);
        return Result.success(null);
    }

    @Override
    public Result<Void> discard(Long id) {
        skillRevisionService.discard(id);
        return Result.success(null);
    }

    @PostMapping("/{id}/rollback")
    @Operation(summary = "回滚到指定修订", description = "以历史修订快照新建发布并生效")
    public Result<Void> rollback(@Parameter(description = "历史修订主键") @PathVariable Long id) {
        skillRevisionService.rollback(id);
        return Result.success(null);
    }

    @PostMapping("/save")
    @IdentityInject(organIdPropertyName = "organId")
    @Operation(summary = "新增或更新Skill 修订表信息", description = "新增或更新Skill 修订表基础信息")
    public Result<Long> save(@RequestBody SkillRevisionDto dto) {
        return Result.success(skillRevisionService.save(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除Skill 修订表记录", description = "根据主键删除Skill 修订表记录")
    public Result<Integer> delete(@Parameter(description = "Skill 修订表标识") @PathVariable Long id) {
        return Result.success(skillRevisionService.delete(id));
    }
}
