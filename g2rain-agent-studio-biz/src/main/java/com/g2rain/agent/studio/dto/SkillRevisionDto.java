package com.g2rain.agent.studio.dto;

import com.g2rain.common.model.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.g2rain.common.validation.CreateGroup;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Skill 修订表查询DTO
 * 表名: skill_revision
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Skill 修订表 DTO")
public class SkillRevisionDto extends BaseDto {

    /**
     * 机构标识
     */
    @NotNull(groups = CreateGroup.class)
    @Schema(description = "机构标识（新增必填）")
    private Long organId;

    /**
     * 技能主键（skill_profile.id）
     */
    @NotNull(groups = CreateGroup.class)
    @Schema(description = "技能主键（skill_profile.id）（新增必填）")
    private Long skillId;

    /**
     * 修订号；草稿为0，发布时从1递增
     */
    @NotNull(groups = CreateGroup.class)
    @Schema(description = "修订号；草稿为0，发布时从1递增（新增必填）")
    private Integer revisionNo;

    /**
     * 快照：技能名称
     */
    @NotBlank(groups = CreateGroup.class)
    @Size(max = 128)
    @Schema(description = "快照：技能名称（新增必填）", maxLength = 128)
    private String skillName;

    /**
     * 快照：语义描述
     */
    @Size(max = 1000)
    @Schema(description = "快照：语义描述", maxLength = 1000)
    private String semanticDesc;

    /**
     * 状态[DRAFT:草稿, PUBLISHED:当前发布, SUPERSEDED:已被替代, DISCARDED:已丢弃]
     */
    @NotBlank(groups = CreateGroup.class)
    @Size(max = 32)
    @Schema(description = "状态[DRAFT:草稿, PUBLISHED:当前发布, SUPERSEDED:已被替代, DISCARDED:已丢弃]（新增必填）", maxLength = 32)
    private String status;

    /**
     * 来源方式[MANUAL:人工保存, CANDIDATE:候选草稿, ROLLBACK:回滚]
     */
    @NotBlank(groups = CreateGroup.class)
    @Size(max = 32)
    @Schema(description = "来源方式[MANUAL:人工保存, CANDIDATE:候选草稿, ROLLBACK:回滚]（新增必填）", maxLength = 32)
    private String origin;

    /**
     * 草稿基于的已发布修订号
     */
    @Schema(description = "草稿基于的已发布修订号")
    private Integer baseRevisionNo;

    /**
     * 变更说明
     */
    @Size(max = 500)
    @Schema(description = "变更说明", maxLength = 500)
    private String note;

    /**
     * 快照：操作手册
     */
    @Size(max = 16777215)
    @Schema(description = "快照：操作手册", maxLength = 16777215)
    private String skillManual;

    /**
     * 快照：执行契约
     */
    @Size(max = 1073741824)
    @Schema(description = "快照：执行契约", maxLength = 1073741824)
    private String skillContract;

    /**
     * 快照：绑定的业务工具名数组
     */
    @Size(max = 1073741824)
    @Schema(description = "快照：绑定的业务工具名数组", maxLength = 1073741824)
    private String toolNames;
}
