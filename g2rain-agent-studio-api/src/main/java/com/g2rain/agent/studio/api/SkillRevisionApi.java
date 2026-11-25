package com.g2rain.agent.studio.api;

import com.g2rain.agent.studio.dto.CreateSkillRevisionDraftDto;
import com.g2rain.agent.studio.dto.PublishSkillRevisionDto;
import com.g2rain.agent.studio.dto.SkillRevisionSelectDto;
import com.g2rain.agent.studio.vo.SkillRevisionVo;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


/**
 * Skill 修订表API接口
 * 表名: skill_revision
 *
 * @author G2rain Generator
 */
@Tag(name = "Skill 修订表", description = "Skill 修订表相关接口")
public interface SkillRevisionApi {

    /**
     * 根据条件查询列表
     *
     * @param selectDto 查询条件 DTO
     * @return 数据列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询Skill 修订表列表", description = "根据查询条件返回Skill 修订表列表")
    Result<List<SkillRevisionVo>> selectList(SkillRevisionSelectDto selectDto);

    /**
     * 根据条件分页查询
     *
     * @param selectDto 查询条件DTO（包含分页参数）
     * @return 分页数据
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询Skill 修订表列表", description = "分页查询Skill 修订表列表")
    Result<PageData<SkillRevisionVo>> selectPage(PageSelectListDto<SkillRevisionSelectDto> selectDto);

    /**
     * 按主键查询修订详情。
     *
     * @param id 修订主键
     * @return 修订
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询Skill 修订详情", description = "根据主键查询修订详情")
    Result<SkillRevisionVo> selectById(@Parameter(description = "修订主键") @PathVariable("id") Long id);

    /**
     * 以当前发布修订为底创建草稿，只覆盖入参中的字段。
     *
     * @param dto 入参
     * @return 草稿修订主键
     */
    @PostMapping("/draft")
    @Operation(summary = "创建技能修订草稿", description = "以当前发布修订为底覆盖字段生成候选草稿")
    Result<Long> createDraft(@RequestBody CreateSkillRevisionDraftDto dto);

    /**
     * 丢弃草稿修订。
     *
     * @param id 草稿主键
     * @return 空
     */
    @PostMapping("/{id}/discard")
    @Operation(summary = "丢弃技能修订草稿", description = "草稿改为已丢弃")
    Result<Void> discard(@Parameter(description = "草稿主键") @PathVariable("id") Long id);

    /**
     * 发布草稿修订。
     *
     * @param id  草稿主键
     * @param dto 入参（含期望的当前发布修订号）
     * @return 空
     */
    @PostMapping("/{id}/publish")
    @Operation(summary = "发布技能修订草稿", description = "快照写回技能与绑定，草稿升为发布")
    Result<Void> publish(@Parameter(description = "草稿主键") @PathVariable("id") Long id,
                         @RequestBody PublishSkillRevisionDto dto);
}