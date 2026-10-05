package com.g2rain.agent.studio.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Skill 短目录项（意图选人用；不含手册 / 契约 / tools）。
 */
@Getter
@Setter
@NoArgsConstructor
public class SkillDigestVo {

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
}
