package com.g2rain.agent.studio.dto;

import com.g2rain.common.enums.SessionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * 智能体访问授权 DTO（全量提交）
 */
@Setter
@Getter
@NoArgsConstructor
@Schema(description = "智能体访问授权 DTO")
public class AgentSessionAssignDto {

    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "数字员工标识")
    private Long agentId;

    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "可访问的会话主体类型（全量）；空集合表示全部取消")
    private Set<SessionType> sessionTypes;
}
