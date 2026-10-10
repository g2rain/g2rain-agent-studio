package com.g2rain.agent.studio.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 数字员工下拉选项（仅 id + 名称，省带宽）。
 */
@Getter
@Setter
@NoArgsConstructor
public class AgentOptionVo {

    /**
     * 数字员工标识
     */
    private Long id;

    /**
     * 数字员工名称
     */
    private String agentName;
}
