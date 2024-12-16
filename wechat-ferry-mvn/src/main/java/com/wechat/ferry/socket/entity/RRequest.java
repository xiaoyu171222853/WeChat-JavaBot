package com.wechat.ferry.socket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RRequest {
    private String type;    // 请求类型 Get/Post

    private String method;  // 请求方法，客户端中具体方法

    private Object params;  // 请求参数
}
