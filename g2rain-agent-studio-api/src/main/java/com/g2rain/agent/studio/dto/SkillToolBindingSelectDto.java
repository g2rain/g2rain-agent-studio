package com.g2rain.agent.studio.dto;

import com.g2rain.common.model.BaseSelectListDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


/**
 * Skill Tool 绑定表查询入参DTO
 * 用于SkillToolBindingDao.selectList方法的条件筛选
 * 表名: skill_tool_binding
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Skill Tool 绑定表查询入参 DTO")
public class SkillToolBindingSelectDto extends BaseSelectListDto {

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
     * Skill 主键集合（批量，能力包装载用）
     */
    @Schema(description = "Skill 主键集合")
    private Set<Long> skillIds;

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
}
