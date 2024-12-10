package com.wechat.ferry.entity;

import com.wechat.ferry.enums.MessageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private MessageTypeEnum type;  // 消息类型
    private String content;        // 消息内容
    private String sender;         // 发送者
    private String receiver;       // 接收者
    // 转换为 JSON 格式（或其他格式）
    public String toJson() {
        return String.format("{\"type\":\"%s\", \"content\":\"%s\", \"sender\":\"%s\", \"receiver\":\"%s\"}",
                this.type.getValue(), this.content, this.sender, this.receiver);
    }
}
