package com.g2rain.agent.studio.dao.po;

import com.g2rain.common.model.BasePo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 数字员工画像表返回Po
 * 关联表名: agent_profile
 * 功能：封装实体数据，继承BasePo复用基础字段逻辑
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AgentProfilePo extends BasePo {

    /**
     * 机构标识
     */
    private Long organId;

    /**
     * 数字员工编码
     */
    private String agentCode;

    /**
     * 数字员工名称
     */
    private String agentName;

    /**
     * 资源类型[PLATFORM:G2rain员工, CUSTOM:租户自定义/安装副本]
     */
    private String resourceType;

    /**
     * 身份定义
     */
    private String identityDesc;

    /**
     * 任务使命
     */
    private String missionDesc;

    /**
     * 边界约束
     */
    private String boundaryDesc;

    /**
     * 交互风格
     */
    private String styleDesc;

    /**
     * 行为原则
     */
    private String principleDesc;

    /**
     * 状态[ACTIVE:启用, INACTIVE:禁用]
     */
    private String status;

    /**
     * 备注
     */
    private String description;

    /**
     * 逻辑删除标记[0:未删除, 1:已删除]
     */
    private Boolean deleteFlag;
}