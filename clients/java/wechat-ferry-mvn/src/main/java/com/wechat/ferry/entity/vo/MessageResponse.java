package com.wechat.ferry.entity.vo;

import com.wechat.ferry.enums.MessageTypeEnum;
import lombok.Getter;

@Getter
public class MessageResponse<T> {
    private MessageTypeEnum messageTypeEnum;
    private T response;
}
