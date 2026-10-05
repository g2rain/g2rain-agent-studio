package com.g2rain.agent.studio.dto;

import com.g2rain.common.model.BaseSelectListDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 数字员工 Skill 绑定表查询入参DTO
 * 用于AgentSkillBindingDao.selectList方法的条件筛选
 * 表名: agent_skill_binding
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "数字员工 Skill 绑定表查询入参 DTO")
public class AgentSkillBindingSelectDto extends BaseSelectListDto {

    /**
     * 机构标识
     */
    @Schema(description = "机构标识")
    private Long organId;

    /**
     * 数字员工主键（agent_profile.id）
     */
    @Schema(description = "数字员工主键（agent_profile.id）")
    private Long agentId;

    /**
     * Skill 主键（skill_profile.id）
     */
    @Schema(description = "Skill 主键（skill_profile.id）")
    private Long skillId;

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
