package com.g2rain.agent.studio.vo;

import com.g2rain.common.json.AdminCompanyCondition;
import com.g2rain.common.json.ConditionalJsonIgnore;
import com.g2rain.common.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Skill 定义表返回VO
 * 关联表名: skill_profile
 * 功能：封装接口返回数据，继承BaseVo复用基础字段逻辑，隔离数据库实体与前端展示层
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Skill 定义表 VO")
public class SkillProfileVo extends BaseVo {

    /**
     * 机构标识
     */
    @Schema(description = "机构标识")
    private Long organId;

    /**
     * Skill 唯一键，如 crm.quote.query
     */
    @Schema(description = "Skill 唯一键，如 crm.quote.query")
    private String skillKey;

    /**
     * Skill 名称
     */
    @Schema(description = "Skill 名称")
    private String skillName;

    /**
     * 资源类型[PLATFORM:G2rain员工, CUSTOM:租户自定义/安装副本]
     */
    @Schema(description = "资源类型[PLATFORM:G2rain员工, CUSTOM:租户自定义/安装副本]")
    private String resourceType;

    /**
     * Skill 语义描述
     */
    @Schema(description = "Skill 语义描述")
    private String semanticDesc;

    @Schema(description = "Skill 操作手册（注入 LLM）")
    private String skillManual;

    @Schema(description = "Skill 执行契约 JSON")
    private String skillContract;

    /**
     * 状态[ACTIVE:启用, INACTIVE:禁用]
     */
    @Schema(description = "状态[ACTIVE:启用, INACTIVE:禁用]")
    private String status;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String description;

    /**
     * 逻辑删除标记[0:未删除, 1:已删除]
     */
    @Schema(description = "删除标识（0 未删除，1 已删除）", example = "false")
    @ConditionalJsonIgnore(adminCompany = AdminCompanyCondition.TRUE)
    private Boolean deleteFlag;
}
