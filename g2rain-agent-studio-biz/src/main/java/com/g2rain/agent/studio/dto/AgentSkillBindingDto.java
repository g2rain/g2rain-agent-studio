package com.g2rain.agent.studio.dto;

import com.g2rain.common.model.BaseDto;
import com.g2rain.common.validation.CreateGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 数字员工 Skill 绑定表查询DTO
 * 表名: agent_skill_binding
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "数字员工 Skill 绑定表 DTO")
public class AgentSkillBindingDto extends BaseDto {

    /**
     * 机构标识
     */
    @NotNull(groups = CreateGroup.class)
    @Schema(description = "机构标识（新增必填）")
    private Long organId;

    /**
     * 数字员工主键（agent_profile.id）
     */
    @NotNull(groups = CreateGroup.class)
    @Schema(description = "数字员工主键（agent_profile.id）（新增必填）")
    private Long agentId;

    /**
     * Skill 主键（skill_profile.id）
     */
    @NotNull(groups = CreateGroup.class)
    @Schema(description = "Skill 主键（skill_profile.id）（新增必填）")
    private Long skillId;

    /**
     * 是否启用[0:否, 1:是]
     */
    @NotNull(groups = CreateGroup.class)
    @Schema(description = "是否启用[0:否, 1:是]（新增必填）")
    private Byte enabledFlag;

    /**
     * 备注
     */
    @Size(max = 500)
    @Schema(description = "备注", maxLength = 500)
    private String description;
}
