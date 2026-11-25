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
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/{id}")
    @Operation(summary = "查询Skill 修订详情", description = "根据主键查询修订详情")
    public Result<SkillRevisionVo> selectById(@Parameter(description = "修订主键") @PathVariable Long id) {
        return Result.success(skillRevisionService.selectById(id));
    }

    @PostMapping("/draft")
    @IdentityInject(organIdPropertyName = "organId")
    @Operation(summary = "创建技能修订草稿", description = "以当前发布修订为底覆盖字段生成候选草稿")
    public Result<Long> createDraft(@RequestBody CreateSkillRevisionDraftDto dto) {
        return Result.success(skillRevisionService.createDraft(dto));
    }

    @PostMapping("/{id}/publish")
    @Operation(summary = "发布技能修订草稿", description = "快照写回技能与绑定，草稿升为发布")
    public Result<Void> publish(@Parameter(description = "草稿主键") @PathVariable Long id,
                                @RequestBody PublishSkillRevisionDto dto) {
        skillRevisionService.publish(id, dto);
        return Result.success(null);
    }

    @PostMapping("/{id}/discard")
    @Operation(summary = "丢弃技能修订草稿", description = "草稿改为已丢弃")
    public Result<Void> discard(@Parameter(description = "草稿主键") @PathVariable Long id) {
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
