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
 * 智能体会话绑定表查询DTO
 * 表名: agent_session_binding
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "智能体会话绑定表 DTO")
public class AgentSessionBindingDto extends BaseDto {

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
     * 会话主体类型[SessionType: USER|MEMBER|PASSPORT|ANONYMOUS]
     */
    @NotBlank(groups = CreateGroup.class)
    @Size(max = 32)
    @Schema(description = "会话主体类型[SessionType: USER|MEMBER|PASSPORT|ANONYMOUS]（新增必填）", maxLength = 32)
    private String sessionType;
}
