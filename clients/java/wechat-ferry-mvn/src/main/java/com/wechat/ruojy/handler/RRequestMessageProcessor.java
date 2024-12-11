package com.wechat.ruojy.handler;

import com.alibaba.fastjson2.JSON;
import com.wechat.ruojy.entity.Message;
import com.wechat.ruojy.entity.RRequest;
import com.wechat.ruojy.entity.RResponse;
import com.wechat.ruojy.enums.MessageTypeEnum;
import com.wechat.ruojy.handler.methodHandler.HandlerRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;

/**
 * 服务端调用请求处理
 */
@Component
public class RRequestMessageProcessor {
    private final HandlerRegistry handlerRegistry;

    @Value("${ruojy.token}")
    private String TOKEN;

    public RRequestMessageProcessor(HandlerRegistry handlerRegistry) {
        this.handlerRegistry = handlerRegistry;
    }

    public void processRequest(Message message,WebSocketSession session) throws IOException {
        // 获取处理器
        RRequest request = JSON.parseObject(message.getContent(), RRequest.class);
        String method = request.getMethod();
        MethodHandler handler = handlerRegistry.getHandler(method);

        if (handler == null) {
            throw new IllegalArgumentException("No handler found for method: " + method);
        }

        Map<String, Object> params = (Map<String, Object>) request.getParams();
        Object handle = handler.handle(params);
        // 返回回复
        sendResponse(session,message.getRequestId(),RResponse.success(handle));
    }

    // 向服务端返回结果
    private void sendResponse(WebSocketSession session,String requestId,RResponse result) throws IOException {
        Message message = new Message();
        message.setRequestId(requestId);
        message.setType(MessageTypeEnum.RESPONSE);
        message.setToken(TOKEN);
        message.setContent(JSON.toJSONString(result));
        session.sendMessage(new TextMessage(JSON.toJSONString(message)));
    }
}
