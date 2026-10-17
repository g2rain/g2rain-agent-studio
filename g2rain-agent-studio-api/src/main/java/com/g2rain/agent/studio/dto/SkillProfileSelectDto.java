package com.g2rain.agent.studio.dto;

import com.g2rain.common.model.BaseSelectListDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Skill 定义表查询入参DTO
 * 用于SkillProfileDao.selectList方法的条件筛选
 * 表名: skill_profile
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Skill 定义表查询入参 DTO")
public class SkillProfileSelectDto extends BaseSelectListDto {

    /**
     * 机构标识
     */
    @Schema(description = "机构标识")
    private Long organId;

    /**
     * 所属数字员工主键（agent_profile.id）
     */
    @Schema(description = "所属数字员工主键")
    private Long agentId;

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
}
