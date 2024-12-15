package com.wechat.ruojy.enums;

import lombok.Getter;

@Getter
public enum MessageTypeEnum {
    NOTIFICATION("通知消息"),
    REQUEST("请求消息"),
    RESPONSE("回复消息"),
    HEARTBEAT("心跳检测消息");

    // 描述
    private final String description;


    // 构造方法
    MessageTypeEnum(String description) {
        this.description = description;
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
