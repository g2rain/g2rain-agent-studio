package com.g2rain.agent.studio.dto;

import com.g2rain.common.model.BaseDto;
import com.g2rain.common.validation.CreateGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Skill 定义表查询DTO
 * 表名: skill_profile
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Skill 定义表 DTO")
public class SkillProfileDto extends BaseDto {

    /**
     * 机构标识
     */
    @NotNull(groups = CreateGroup.class)
    @Schema(description = "机构标识（新增必填）")
    private Long organId;

    /**
     * 所属数字员工主键（agent_profile.id）
     */
    @NotNull(groups = CreateGroup.class)
    @Schema(description = "所属数字员工主键（新增必填）")
    private Long agentId;

    /**
     * Skill 唯一键，如 crm.quote.query
     */
    @NotBlank(groups = CreateGroup.class)
    @Size(max = 128)
    @Schema(description = "Skill 唯一键，如 crm.quote.query（新增必填）", maxLength = 128)
    private String skillKey;

    /**
     * Skill 名称
     */
    @NotBlank(groups = CreateGroup.class)
    @Size(max = 128)
    @Schema(description = "Skill 名称（新增必填）", maxLength = 128)
    private String skillName;

    /**
     * 资源类型[PLATFORM:G2rain员工, CUSTOM:租户自定义/安装副本]
     */
    @NotBlank(groups = CreateGroup.class)
    @Size(max = 32)
    @Schema(description = "资源类型[PLATFORM:G2rain员工, CUSTOM:租户自定义/安装副本]（新增必填）", maxLength = 32)
    private String resourceType;

    /**
     * Skill 语义描述
     */
    @Size(max = 1000)
    @Schema(description = "Skill 语义描述", maxLength = 1000)
    private String semanticDesc;

    @Schema(description = "Skill 操作手册（注入 LLM）")
    private String skillManual;

    @Schema(description = "Skill 执行契约 JSON")
    private String skillContract;

    /**
     * 备注
     */
    @Size(max = 500)
    @Schema(description = "备注", maxLength = 500)
    private String description;
}
