package com.g2rain.agent.studio.dto;

import com.g2rain.common.model.BaseSelectListDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 数字员工画像表查询入参DTO
 * 用于AgentProfileDao.selectList方法的条件筛选
 * 表名: agent_profile
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "数字员工画像表查询入参 DTO")
public class AgentProfileSelectDto extends BaseSelectListDto {

    /**
     * 机构标识
     */
    @Schema(description = "机构标识")
    private Long organId;

    /**
     * 数字员工编码
     */
    @Schema(description = "数字员工编码")
    private String agentCode;

    /**
     * 数字员工名称
     */
    @Schema(description = "数字员工名称")
    private String agentName;

    /**
     * 资源类型[PLATFORM:G2rain员工, CUSTOM:租户自定义/安装副本]
     */
    @Schema(description = "资源类型[PLATFORM:G2rain员工, CUSTOM:租户自定义/安装副本]")
    private String resourceType;

    /**
     * 身份定义
     */
    @Schema(description = "身份定义")
    private String identityDesc;

    /**
     * 任务使命
     */
    @Schema(description = "任务使命")
    private String missionDesc;

    /**
     * 边界约束
     */
    @Schema(description = "边界约束")
    private String boundaryDesc;

    /**
     * 交互风格
     */
    @Schema(description = "交互风格")
    private String styleDesc;

    /**
     * 行为原则
     */
    @Schema(description = "行为原则")
    private String principleDesc;

    /**
     * 状态[ACTIVE:启用, INACTIVE:禁用]
     */
    @Schema(description = "状态[ACTIVE:启用, INACTIVE:禁用]")
    private String status;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String description;

    /**
     * 当前会话主体类型（服务端注入，限定可访问的智能体；为空时不匹配任何智能体）
     */
    @Schema(hidden = true)
    private String sessionType;
}
