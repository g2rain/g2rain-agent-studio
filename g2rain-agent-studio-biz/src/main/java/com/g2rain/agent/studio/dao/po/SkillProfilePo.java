package com.g2rain.agent.studio.dao.po;

import com.g2rain.common.model.BasePo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Skill 定义表返回Po
 * 关联表名: skill_profile
 * 功能：封装实体数据，继承BasePo复用基础字段逻辑
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SkillProfilePo extends BasePo {

    /**
     * 机构标识
     */
    private Long organId;

    /**
     * 所属数字员工主键（agent_profile.id）
     */
    private Long agentId;

    /**
     * Skill 唯一键，如 crm.quote.query
     */
    private String skillKey;

    /**
     * Skill 名称
     */
    private String skillName;

    /**
     * 资源类型[PLATFORM:G2rain员工, CUSTOM:租户自定义/安装副本]
     */
    private String resourceType;

    /**
     * Skill 语义描述
     */
    private String semanticDesc;

    /**
     * Skill 操作手册（注入 LLM）
     */
    private String skillManual;

    /**
     * Skill 执行契约 JSON
     */
    private String skillContract;

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
