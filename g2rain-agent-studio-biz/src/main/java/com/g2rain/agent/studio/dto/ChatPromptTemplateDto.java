package com.g2rain.agent.studio.dto;

import com.g2rain.common.model.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.g2rain.common.validation.CreateGroup;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Chat 问题模板查询DTO
 * 表名: chat_prompt_template
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Chat 问题模板 DTO")
public class ChatPromptTemplateDto extends BaseDto {

    /**
     * 机构标识
     */
    @NotNull(groups = CreateGroup.class)
    @Schema(description = "机构标识（新增必填）")
    private Long organId;

    /**
     * 问题内容
     */
    @NotBlank(groups = CreateGroup.class)
    @Size(max = 1000)
    @Schema(description = "问题内容（新增必填）", maxLength = 1000)
    private String promptText;

    /**
     * 展示顺序，数值越小越靠前
     */
    @NotNull(groups = CreateGroup.class)
    @Schema(description = "展示顺序，数值越小越靠前（新增必填）")
    private Integer sortOrder;

    /**
     * 状态[ACTIVE:启用, INACTIVE:禁用]
     */
    @NotBlank(groups = CreateGroup.class)
    @Size(max = 32)
    @Schema(description = "状态[ACTIVE:启用, INACTIVE:禁用]（新增必填）", maxLength = 32)
    private String status;

    /**
     * 备注
     */
    @Size(max = 500)
    @Schema(description = "备注", maxLength = 500)
    private String description;
}
