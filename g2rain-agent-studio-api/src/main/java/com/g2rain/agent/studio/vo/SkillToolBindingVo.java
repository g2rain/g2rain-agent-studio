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
 * Skill Tool 绑定表返回VO
 * 关联表名: skill_tool_binding
 * 功能：封装接口返回数据，继承BaseVo复用基础字段逻辑，隔离数据库实体与前端展示层
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Skill Tool 绑定表 VO")
public class SkillToolBindingVo extends BaseVo {

    /**
     * 机构标识
     */
    @Schema(description = "机构标识")
    private Long organId;

    /**
     * Skill主键（skill_profile.id）
     */
    @Schema(description = "Skill主键（skill_profile.id）")
    private Long skillId;

    /**
     * Tool 名称（MCP tools/list）
     */
    @Schema(description = "Tool 名称（MCP tools/list）")
    private String toolName;

    /**
     * 是否启用[0:否, 1:是]
     */
    @Schema(description = "是否启用[0:否, 1:是]")
    private Byte enabledFlag;

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
