package com.g2rain.agent.studio.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.g2rain.common.model.BaseSelectListDto;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * 智能体会话绑定表查询入参DTO
 * 用于AgentSessionBindingDao.selectList方法的条件筛选
 * 表名: agent_session_binding
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "智能体会话绑定表查询入参 DTO")
public class AgentSessionBindingSelectDto extends BaseSelectListDto {

    /**
     * 机构标识
     */
    @Schema(description = "机构标识")
    private Long organId;

    /**
     * 数字员工主键（agent_profile.id）
     */
    @Schema(description = "数字员工主键（agent_profile.id）")
    private Long agentId;

    /**
     * 会话主体类型[SessionType: USER|MEMBER|PASSPORT|ANONYMOUS]
     */
    @Schema(description = "会话主体类型[SessionType: USER|MEMBER|PASSPORT|ANONYMOUS]")
    private String sessionType;
}