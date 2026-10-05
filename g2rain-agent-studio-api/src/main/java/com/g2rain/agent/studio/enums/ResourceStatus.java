package com.g2rain.agent.studio.enums;

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
}
