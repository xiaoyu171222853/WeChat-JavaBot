package com.wechat.ferry.entity.vo;

import com.wechat.ferry.entity.vo.request.WxPpWcfRequest;
import lombok.Getter;
import com.wechat.ferry.entity.vo.request.*;
import com.wechat.ferry.enums.MessageTypeEnum;

@Getter
public class MessageRequest {

    private final MessageTypeEnum messageTypeEnum;
    private final Object request;

    private MessageRequest(MessageTypeEnum messageTypeEnum, Object request) {
        if (isValidRequestType(messageTypeEnum, request)) {
            throw new IllegalArgumentException("Invalid request type for message type: " + messageTypeEnum);
        }
        this.messageTypeEnum = messageTypeEnum;
        this.request = request;
    }

    // 限定范型上限
    public static <T extends WxPpWcfRequest> MessageRequest create(MessageTypeEnum messageType, T request) {
        if (isValidRequestType(messageType, request)) {
            throw new IllegalArgumentException("Invalid request type for message type: " + messageType);
        }
        return new MessageRequest(messageType, request);
    }

    // 校验request类型是否符合messageTypeEnum的要求
    private static boolean isValidRequestType(MessageTypeEnum messageTypeEnum, Object request) {
        return !messageTypeEnum.isValidRequest(request);
    }
}


