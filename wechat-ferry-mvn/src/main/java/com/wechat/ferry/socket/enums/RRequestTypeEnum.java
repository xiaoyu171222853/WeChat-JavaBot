package com.wechat.ferry.socket.enums;

import lombok.Getter;

@Getter
public enum RRequestTypeEnum {
    GET("get请求"),
    POST("Post请求");

    // 获取描述
    private final String description;

    // 构造方法
    RRequestTypeEnum(String description) {
        this.description = description;
    }
}
