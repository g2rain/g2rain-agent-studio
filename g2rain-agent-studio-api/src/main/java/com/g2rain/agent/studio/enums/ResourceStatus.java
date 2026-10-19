package com.g2rain.agent.studio.enums;

import com.g2rain.common.exception.BusinessException;
import com.g2rain.common.exception.SystemErrorCode;
import com.g2rain.common.utils.Strings;

/**
 * 资源状态
 */
public enum ResourceStatus {

    /**
     * 启用
     */
    ACTIVE,

    /**
     * 禁用
     */
    INACTIVE;

    public static boolean isInactive(String status) {
        return !ResourceStatus.ACTIVE.name().equals(status);
    }

    /**
     * 校验状态名是否为合法枚举值。
     *
     * @param name 状态字符串
     */
    public static void validate(String name) {
        if (Strings.isNotBlank(name)) {
            for (ResourceStatus status : values()) {
                if (status.name().equals(name)) {
                    return;
                }
            }
        }
        throw new BusinessException(SystemErrorCode.PARAM_VAL_INVALID, name);
    }
}
