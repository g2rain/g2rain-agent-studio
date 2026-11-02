package com.g2rain.agent.studio.dao.po;

import com.g2rain.common.model.BasePo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Chat 问题模板返回Po
 * 关联表名: chat_prompt_template
 * 功能：封装实体数据，继承BasePo复用基础字段逻辑
 *
 * @author G2rain Generator
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChatPromptTemplatePo extends BasePo {

    /**
     * 机构标识
     */
    private Long organId;

    /**
     * 问题内容
     */
    private String promptText;

    /**
     * 展示顺序，数值越小越靠前
     */
    private Integer sortOrder;

    /**
     * 状态[ACTIVE:启用, INACTIVE:禁用]
     */
    private String status;

    /**
     * 备注
     */
    private String description;

    /**
     * 逻辑删除标记[0:未删除, 1:已删除]
     */
    private Boolean deleteFlag;
}