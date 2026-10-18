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
 * Skill Tool 绑定表查询DTO
 * 表名: skill_tool_binding
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Skill Tool 绑定表 DTO")
public class SkillToolBindingDto extends BaseDto {

    /**
     * 机构标识
     */
    @NotNull(groups = CreateGroup.class)
    @Schema(description = "机构标识（新增必填）")
    private Long organId;

    /**
     * Skill主键（skill_profile.id）
     */
    @NotNull(groups = CreateGroup.class)
    @Schema(description = "Skill主键（skill_profile.id）（新增必填）")
    private Long skillId;

    /**
     * Tool 名称（MCP tools/list）
     */
    @NotBlank(groups = CreateGroup.class)
    @Size(max = 256)
    @Schema(description = "Tool 名称（MCP tools/list）（新增必填）", maxLength = 256)
    private String toolName;

    /**
     * 备注
     */
    @Size(max = 500)
    @Schema(description = "备注", maxLength = 500)
    private String description;
}
