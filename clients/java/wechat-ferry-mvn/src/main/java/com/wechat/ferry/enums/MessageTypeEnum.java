package com.wechat.ferry.enums;

import org.springframework.util.ObjectUtils;

public enum MessageTypeEnum {
    TEXT("文本消息","TEXT"),
    BINARY("二进制消息","BINARY"),
    NOTIFICATION("通知消息","NOTIFICATION"),
    HEARTBEAT("心跳检测消息","HEARTBEAT"),
    ERROR("错误消息","ERROR");

    private final String description;
    private final String value;
    // 获取 value
    public String getValue() {
        return value;
    }
    // 构造方法
    MessageTypeEnum(String description,String value) {
        this.description = description;
        this.value = value;
    }

    private static MessageTypeEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (MessageTypeEnum messageType : MessageTypeEnum.values()) {
            if (messageType.value.equals(value)) {
                return messageType;
            }
        }
        return null;
    }

    // 根据消息类型获取枚举
    public static MessageTypeEnum fromString(String type) {
        for (MessageTypeEnum messageType : MessageTypeEnum.values()) {
            if (messageType.name().equalsIgnoreCase(type)) {
                return messageType;
            }
        }
        return null;  // 如果没有匹配到，返回 null 或者可以抛出异常
    }
}
