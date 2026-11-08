package com.g2rain.agent.studio.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.g2rain.common.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;

import com.g2rain.common.json.ConditionalJsonIgnore;
import com.g2rain.common.json.AdminCompanyCondition;

/**
 * 智能体会话绑定表返回VO
 * 关联表名: agent_session_binding
 * 功能：封装接口返回数据，继承BaseVo复用基础字段逻辑，隔离数据库实体与前端展示层
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "智能体会话绑定表 VO")
public class AgentSessionBindingVo extends BaseVo {

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

    /**
     * 逻辑删除标记[0:未删除, 1:已删除]
     */
    @Schema(description = "删除标识（0 未删除，1 已删除）", example = "false")
    @ConditionalJsonIgnore(adminCompany = AdminCompanyCondition.TRUE)
    private Boolean deleteFlag;
}