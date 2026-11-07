package com.g2rain.agent.studio.dao.po;

import com.g2rain.common.model.BasePo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 智能体会话绑定表返回Po
 * 关联表名: agent_session_binding
 * 功能：封装实体数据，继承BasePo复用基础字段逻辑
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AgentSessionBindingPo extends BasePo {

    /**
     * 机构标识
     */
    private Long organId;

    /**
     * 数字员工主键（agent_profile.id）
     */
    private Long agentId;

    /**
     * 会话主体类型[SessionType: USER|MEMBER|PASSPORT|ANONYMOUS]
     */
    private String sessionType;

    /**
     * 逻辑删除标记[0:未删除, 1:已删除]
     */
    private Boolean deleteFlag;
}