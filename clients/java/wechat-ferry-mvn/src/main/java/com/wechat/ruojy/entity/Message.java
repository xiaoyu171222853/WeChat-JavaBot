package com.wechat.ruojy.entity;

import com.alibaba.fastjson2.JSON;
import com.wechat.ruojy.enums.MessageTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private MessageTypeEnum type;  // 消息类型
    private String content;        // 消息内容
    private String token;     // 请求方token
    @ApiModelProperty("请求Id")
    private String requestId;
    public static Message parseMessage(String messageStr) {
        // 解析消息并返回 Message 对象
        return JSON.parseObject(messageStr, Message.class);
    }
}
