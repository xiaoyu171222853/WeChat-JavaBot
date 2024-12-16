package com.wechat.ferry.socket.handler;

import com.alibaba.fastjson2.JSON;
import com.wechat.ferry.socket.entity.Message;
import com.wechat.ferry.socket.entity.RRequest;
import com.wechat.ferry.socket.entity.RResponse;
import com.wechat.ferry.socket.enums.RMessageTypeEnum;
import com.wechat.ferry.socket.enums.RRequestTypeEnum;
import com.wechat.ferry.socket.handler.methodHandler.HandlerRegistry;
import com.wechat.ferry.socket.handler.methodHandler.MethodHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务端调用请求处理
 */
@Component
public class RRequestMessageProcessor {

    @Resource
    private HandlerRegistry handlerRegistry;

    @Value("${ruojy.token}")
    private String TOKEN;


    public void processRequest(Message message, WebSocketSession session) throws IOException {
        // 获取处理器
        RRequest request = JSON.parseObject(message.getContent(), RRequest.class);
        String method = request.getMethod();
        MethodHandler handler = handlerRegistry.getHandler(method);

        if (handler == null) {
            throw new IllegalArgumentException("No handler found for method: " + method);
        }
        Map<String, Object> params = new HashMap<>();
        if (request.getType().equals(RRequestTypeEnum.GET.name())){
            // get请求不作参数处理
        }else {
            params=(Map<String, Object>) request.getParams();
        }
        Object handle = handler.handle(params);
        // 返回回复
        sendResponse(session,message.getRequestId(), RResponse.success(handle));
    }

    // 向服务端返回结果
    private void sendResponse(WebSocketSession session,String requestId,RResponse result) throws IOException {
        Message message = new Message();
        message.setRequestId(requestId);
        message.setType(RMessageTypeEnum.RESPONSE);
        message.setToken(TOKEN);
        message.setContent(JSON.toJSONString(result));
        session.sendMessage(new TextMessage(JSON.toJSONString(message)));
    }
}
