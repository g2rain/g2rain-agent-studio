package com.g2rain.agent.studio.enums;

import lombok.Getter;

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
}
