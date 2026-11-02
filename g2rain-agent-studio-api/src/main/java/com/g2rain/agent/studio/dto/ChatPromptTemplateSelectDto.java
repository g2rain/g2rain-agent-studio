package com.g2rain.agent.studio.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.g2rain.common.model.BaseSelectListDto;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * Chat 问题模板查询入参DTO
 * 用于ChatPromptTemplateDao.selectList方法的条件筛选
 * 表名: chat_prompt_template
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Chat 问题模板查询入参 DTO")
public class ChatPromptTemplateSelectDto extends BaseSelectListDto {

    /**
     * 机构标识
     */
    @Schema(description = "机构标识")
    private Long organId;

    /**
     * 问题内容
     */
    @Schema(description = "问题内容")
    private String promptText;

    /**
     * 展示顺序，数值越小越靠前
     */
    @Schema(description = "展示顺序，数值越小越靠前")
    private Integer sortOrder;

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