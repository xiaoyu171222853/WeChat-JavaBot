package com.wechat.ferry.enums;

import com.wechat.ferry.entity.vo.request.*;
import lombok.Getter;

@Getter
public enum MessageTypeEnum {
    TEXT("文本消息", WxPpWcfSendTextMsgReq.class),
    IMAGE("图片消息", WxPpWcfSendImageMsgReq.class),
    FILE("文件消息", WxPpWcfSendFileMsgReq.class),
    RICHTER("富文本消息", WxPpWcfSendRichTextMsgReq.class),
    XML("xml消息", WxPpWcfSendXmlMsgReq.class),
    EMOJI("emoji", WxPpWcfSendEmojiMsgReq.class),;

    final String description;
    private final Class<?> expectedRequestClass;

    MessageTypeEnum(String description, Class<?> expectedRequestClass) {
        this.description=description;
        this.expectedRequestClass = expectedRequestClass;
    }

    public boolean isValidRequest(Object request) {
        return expectedRequestClass.isInstance(request);
    }
}
