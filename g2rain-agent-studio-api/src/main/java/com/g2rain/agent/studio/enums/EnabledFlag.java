package com.g2rain.agent.studio.enums;

import com.g2rain.common.exception.BusinessException;
import com.g2rain.common.exception.SystemErrorCode;
import lombok.Getter;

import java.util.Objects;

/**
 * 是否启用
 */
@Getter
public enum EnabledFlag {

    /**
     * 否
     */
    NO((byte) 0),

    /**
     * 是
     */
    YES((byte) 1);

    private final byte code;

    EnabledFlag(byte code) {
        this.code = code;
    }

    /**
     * 校验启用标记是否为合法枚举值。
     *
     * @param code 0 / 1
     */
    public static void validate(Byte code) {
        if (Objects.nonNull(code)) {
            for (EnabledFlag flag : values()) {
                if (flag.code == code) {
                    return;
                }
            }
        }
        throw new BusinessException(SystemErrorCode.PARAM_VAL_INVALID, code);
    }
}
