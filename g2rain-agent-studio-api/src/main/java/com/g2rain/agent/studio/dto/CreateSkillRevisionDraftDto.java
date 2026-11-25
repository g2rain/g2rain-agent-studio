package com.g2rain.agent.studio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 创建技能修订草稿入参。
 *
 * @author alpha
 * @since 2026/11/25
 */
@Setter
@Getter
@NoArgsConstructor
@Schema(description = "创建技能修订草稿入参")
public class CreateSkillRevisionDraftDto {

    /**
     * 技能主键。
     */
    @NotNull
    @Schema(description = "技能主键", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long skillId;

    /**
     * 覆盖：技能名称。
     */
    @Schema(description = "覆盖：技能名称")
    private String skillName;

    /**
     * 覆盖：语义描述。
     */
    @Schema(description = "覆盖：语义描述")
    private String semanticDesc;

    /**
     * 覆盖：操作手册。
     */
    @Schema(description = "覆盖：操作手册")
    private String skillManual;

    /**
     * 覆盖：执行契约。
     */
    @Schema(description = "覆盖：执行契约")
    private String skillContract;

    /**
     * 覆盖：绑定的业务工具名。
     */
    @Schema(description = "覆盖：绑定的业务工具名")
    private List<String> toolNames;

    /**
     * 变更说明。
     */
    @Schema(description = "变更说明")
    private String note;
}
