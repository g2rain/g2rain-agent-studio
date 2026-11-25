package com.g2rain.agent.studio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 发布技能修订入参。
 *
 * @author alpha
 * @since 2026/11/25
 */
@Setter
@Getter
@NoArgsConstructor
@Schema(description = "发布技能修订入参")
public class PublishSkillRevisionDto {

    /**
     * 期望的当前发布修订号。
     */
    @NotNull
    @Schema(description = "期望的当前发布修订号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer expectedRevisionNo;
}
