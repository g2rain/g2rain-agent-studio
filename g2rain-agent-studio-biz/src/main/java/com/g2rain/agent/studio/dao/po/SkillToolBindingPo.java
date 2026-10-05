package com.g2rain.agent.studio.dao.po;

import com.g2rain.common.model.BasePo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Skill Tool 绑定表返回Po
 * 关联表名: skill_tool_binding
 * 功能：封装实体数据，继承BasePo复用基础字段逻辑
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SkillToolBindingPo extends BasePo {

    /**
     * 机构标识
     */
    private Long organId;

    /**
     * Skill主键（skill_profile.id）
     */
    private Long skillId;

    /**
     * Tool 名称（MCP tools/list）
     */
    private String toolName;

    /**
     * 是否启用[0:否, 1:是]
     */
    private Byte enabledFlag;

    /**
     * 备注
     */
    private String description;

    /**
     * 逻辑删除标记[0:未删除, 1:已删除]
     */
    private Boolean deleteFlag;
}