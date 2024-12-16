package com.wechat.ferry.socket.enums;

import lombok.Getter;

@Getter
public enum RMessageTypeEnum {
    NOTIFICATION("通知消息"),
    REQUEST("请求消息"),
    RESPONSE("回复消息"),
    HEARTBEAT("心跳检测消息");

    // 描述
    private final String description;


    // 构造方法
    RMessageTypeEnum(String description) {
        this.description = description;
    }


    // 根据消息类型获取枚举
    public static RMessageTypeEnum fromString(String type) {
        for (RMessageTypeEnum messageType : RMessageTypeEnum.values()) {
            if (messageType.name().equalsIgnoreCase(type)) {
                return messageType;
            }
        }
        return null;  // 如果没有匹配到，返回 null 或者可以抛出异常
    }
}
