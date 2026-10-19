package com.g2rain.agent.studio.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Agent 能力包：所属 Skill 及其 Tool（不含人设），供 hydrate / 执行。
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgentBundleVo {

    /**
     * 绑定的 Skill 列表（含手册、契约、下属 tools）
     */
    private List<SkillBriefVo> skills = new ArrayList<>();

    /**
     * 单条 Skill 能力摘要。
     */
    @Getter
    @Setter
    @NoArgsConstructor
    public static class SkillBriefVo {
        /**
         * Skill 唯一键
         */
        private String skillKey;
        /**
         * Skill 名称
         */
        private String skillName;
        /**
         * 语义短描述
         */
        private String semanticDesc;
        /**
         * 操作手册
         */
        private String skillManual;
        /**
         * 执行契约 JSON
         */
        private String skillContract;
        /**
         * 该 Skill 绑定的 Tool 列表
         */
        private List<ToolBriefVo> tools = new ArrayList<>();
    }

    /**
     * Skill 下绑定的 Tool。
     */
    @Getter
    @Setter
    @NoArgsConstructor
    public static class ToolBriefVo {
        /**
         * MCP Tool 名称
         */
        private String toolName;
    }
}
