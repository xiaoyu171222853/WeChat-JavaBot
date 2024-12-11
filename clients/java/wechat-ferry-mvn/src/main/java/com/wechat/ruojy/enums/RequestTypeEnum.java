package com.wechat.ruojy.enums;

import lombok.Getter;

@Getter
public enum RequestTypeEnum {
    GET("get请求"),
    POST("Post请求");

    // 获取描述
    private final String description;

    // 构造方法
    RequestTypeEnum(String description) {
        this.description = description;
    }
}
