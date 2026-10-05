package com.g2rain.agent.studio.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 编排用 Agent 画像摘要：ACTIVE 目录 / resolve 解析共用。
 *
 * <p>{@code status} 在 resolve 场景用于区分不存在与未启用；目录接口通常为 ACTIVE。
 */
@Getter
@Setter
@NoArgsConstructor
public class AgentCatalogItemVo {
    /**
     * 数字员工标识
     */
    private Long agentId;

    /**
     * 数字员工编码
     */
    private String agentCode;

    /**
     * 数字员工名称
     */
    private String agentName;

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
}
