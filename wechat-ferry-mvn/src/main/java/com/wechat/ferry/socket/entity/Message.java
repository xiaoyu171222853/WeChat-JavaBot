package com.wechat.ferry.socket.entity;


import com.alibaba.fastjson2.JSON;
import com.wechat.ferry.socket.enums.RMessageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private RMessageTypeEnum type;  // 消息类型
    private String content;        // 消息内容
    private String token;     // 请求方token
    private String requestId;   // 请求Id
    public static Message parseMessage(String messageStr) {
        // 解析消息并返回 Message 对象
        return JSON.parseObject(messageStr, Message.class);
    }
}
