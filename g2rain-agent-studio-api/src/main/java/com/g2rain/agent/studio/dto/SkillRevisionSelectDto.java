package com.g2rain.agent.studio.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.g2rain.common.model.BaseSelectListDto;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * Skill 修订表查询入参DTO
 * 用于SkillRevisionDao.selectList方法的条件筛选
 * 表名: skill_revision
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Skill 修订表查询入参 DTO")
public class SkillRevisionSelectDto extends BaseSelectListDto {

    /**
     * 机构标识
     */
    @Schema(description = "机构标识")
    private Long organId;

    /**
     * 技能主键（skill_profile.id）
     */
    @Schema(description = "技能主键（skill_profile.id）")
    private Long skillId;

    /**
     * 修订号；草稿为0，发布时从1递增
     */
    @Schema(description = "修订号；草稿为0，发布时从1递增")
    private Integer revisionNo;

    /**
     * 快照：技能名称
     */
    @Schema(description = "快照：技能名称")
    private String skillName;

    /**
     * 快照：语义描述
     */
    @Schema(description = "快照：语义描述")
    private String semanticDesc;

    /**
     * 状态[DRAFT:草稿, PUBLISHED:当前发布, SUPERSEDED:已被替代, DISCARDED:已丢弃]
     */
    @Schema(description = "状态[DRAFT:草稿, PUBLISHED:当前发布, SUPERSEDED:已被替代, DISCARDED:已丢弃]")
    private String status;

    /**
     * 来源方式[MANUAL:人工保存, CANDIDATE:候选草稿, ROLLBACK:回滚]
     */
    @Schema(description = "来源方式[MANUAL:人工保存, CANDIDATE:候选草稿, ROLLBACK:回滚]")
    private String origin;

    /**
     * 草稿基于的已发布修订号
     */
    @Schema(description = "草稿基于的已发布修订号")
    private Integer baseRevisionNo;

    /**
     * 变更说明
     */
    @Schema(description = "变更说明")
    private String note;

    /**
     * 快照：操作手册
     */
    @Schema(description = "快照：操作手册")
    private String skillManual;

    /**
     * 快照：执行契约
     */
    @Schema(description = "快照：执行契约")
    private String skillContract;

    /**
     * 快照：绑定的业务工具名数组
     */
    @Schema(description = "快照：绑定的业务工具名数组")
    private String toolNames;
}