package com.g2rain.agent.studio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 启用标记修改 DTO（Skill 工具绑定）
 */
@Setter
@Getter
@NoArgsConstructor
@Schema(description = "启用标记修改 DTO")
public class UpdateEnabledFlagDto {

    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "是否启用[0:否, 1:是]")
    private Byte enabledFlag;
}
