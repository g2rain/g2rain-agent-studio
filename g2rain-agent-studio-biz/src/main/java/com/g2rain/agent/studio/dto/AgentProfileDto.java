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
 * 数字员工画像表查询DTO
 * 表名: agent_profile
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "数字员工画像表 DTO")
public class AgentProfileDto extends BaseDto {

    /**
     * 机构标识
     */
    @NotNull(groups = CreateGroup.class)
    @Schema(description = "机构标识（新增必填）")
    private Long organId;

    /**
     * 数字员工编码
     */
    @NotBlank(groups = CreateGroup.class)
    @Size(max = 64)
    @Schema(description = "数字员工编码（新增必填）", maxLength = 64)
    private String agentCode;

    /**
     * 数字员工名称
     */
    @NotBlank(groups = CreateGroup.class)
    @Size(max = 128)
    @Schema(description = "数字员工名称（新增必填）", maxLength = 128)
    private String agentName;

    /**
     * 资源类型[PLATFORM:G2rain员工, CUSTOM:租户自定义/安装副本]
     */
    @NotBlank(groups = CreateGroup.class)
    @Size(max = 32)
    @Schema(description = "资源类型[PLATFORM:G2rain员工, CUSTOM:租户自定义/安装副本]（新增必填）", maxLength = 32)
    private String resourceType;

    /**
     * 身份定义
     */
    @Size(max = 2000)
    @Schema(description = "身份定义", maxLength = 2000)
    private String identityDesc;

    /**
     * 任务使命
     */
    @Size(max = 2000)
    @Schema(description = "任务使命", maxLength = 2000)
    private String missionDesc;

    /**
     * 边界约束
     */
    @Size(max = 4000)
    @Schema(description = "边界约束", maxLength = 4000)
    private String boundaryDesc;

    /**
     * 交互风格
     */
    @Size(max = 2000)
    @Schema(description = "交互风格", maxLength = 2000)
    private String styleDesc;

    /**
     * 行为原则
     */
    @Size(max = 4000)
    @Schema(description = "行为原则", maxLength = 4000)
    private String principleDesc;

    /**
     * 状态[ACTIVE:启用, INACTIVE:禁用]
     */
    @NotBlank(groups = CreateGroup.class)
    @Size(max = 32)
    @Schema(description = "状态[ACTIVE:启用, INACTIVE:禁用]（新增必填）", maxLength = 32)
    private String status;

    /**
     * 备注
     */
    @Size(max = 500)
    @Schema(description = "备注", maxLength = 500)
    private String description;
}
