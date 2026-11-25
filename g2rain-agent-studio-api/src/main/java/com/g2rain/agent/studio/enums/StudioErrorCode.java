package com.g2rain.agent.studio.enums;

import com.g2rain.common.exception.ErrorCode;

/**
 * Agent Studio 错误码。
 *
 * <p>实现 {@link ErrorCode}，经 {@code BusinessException} 抛出。
 * 编码形如 {@code agent_studio.4xxxx}，新增时顺延，不复用。</p>
 *
 * @author alpha
 * @since 2026/11/25
 */
public enum StudioErrorCode implements ErrorCode {

    /**
     * 发布时期望的发布修订号与当前不一致。
     */
    SKILL_MODIFIED("agent_studio.40001", "技能已被修改：期望修订号 {0:expected}，当前 {1:actual}"),

    /**
     * 修订当前状态不允许该操作。
     */
    REVISION_STATUS_INVALID("agent_studio.40002", "修订状态不允许该操作：{0:revisionId}"),

    /**
     * 修订所属技能不属于指定数字员工。
     */
    REVISION_AGENT_MISMATCH("agent_studio.40003", "修订不属于该数字员工：{0:revisionId}");

    /**
     * 形如 {@code agent_studio.4xxxx} 的错误编码。
     */
    private final String code;

    /**
     * 消息模板，支持 {@code {0:param}} 顺序占位符。
     */
    private final String messageTemplate;

    /**
     * 构造错误码。
     *
     * @param code            错误码
     * @param messageTemplate 消息模板
     */
    StudioErrorCode(String code, String messageTemplate) {
        this.code = code;
        this.messageTemplate = messageTemplate;
    }

    /**
     * 返回错误编码。
     *
     * @return {@code agent_studio.4xxxx} 编码
     */
    @Override
    public String code() {
        return code;
    }

    /**
     * 返回消息模板。
     *
     * @return 带顺序占位符的模板
     */
    @Override
    public String messageTemplate() {
        return messageTemplate;
    }
}
