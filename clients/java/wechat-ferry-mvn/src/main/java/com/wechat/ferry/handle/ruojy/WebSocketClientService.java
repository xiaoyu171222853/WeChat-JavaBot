package com.wechat.ferry.handle.ruojy;

import com.alibaba.fastjson2.JSON;
import com.wechat.ferry.entity.ruojy.Message;
import com.wechat.ferry.enums.ruojy.MessageTypeEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * websocket客户端服务
 */
@Component
public class WebSocketClientService {

    @Value("${ruojy.token}")
    private String myToken;

    @Resource
    private RRequestMessageProcessor RRequestMessageProcessor;
    public void startClient() {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketHandler handler = new WebSocketHandler() {
            /**
             * 建立连接
             * @param session
             */
            @Override
            public void afterConnectionEstablished(WebSocketSession session) {
                System.out.println("Connected to server!");
                try {
//                    session.sendMessage(new TextMessage("Hello from Client"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /**
             * 处理服务端消息
             * @param session
             * @param webSocketMessage
             * @throws IOException
             */
            @Override
            public void handleMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) throws IOException {
                System.out.println("Received from server: " + webSocketMessage.getPayload());
                // 解析消息
                Message message = Message.parseMessage((String) webSocketMessage.getPayload());

                if (message != null) {
                    // 根据消息类型进行处理
                    switch (message.getType()) {
                        case HEARTBEAT:
                            sendMessage(session,new Message(MessageTypeEnum.HEARTBEAT,"PONG",myToken,message.getRequestId()));
                            break;
                        case REQUEST:
                            RRequestMessageProcessor.processRequest(message,session);
                            break;
                        case NOTIFICATION:
                            System.out.println("通知: " + message.getContent());
                            break;
                        default:
                            System.out.println("未知消息类型: " + message.getType());
                    }
                }
            }
            /**
             * 发送消息
             * @param session
             * @param message
             * @throws IOException
             */
            private void sendMessage(WebSocketSession session,Message message) throws IOException {
                session.sendMessage(new TextMessage(JSON.toJSONString(message)));
            }
            @Override
            public void handleTransportError(WebSocketSession session, Throwable exception) {
                System.err.println("Error occurred: " + exception.getMessage());
            }

            /**
             * 连接关闭
             * @param session
             * @param closeStatus
             * @throws Exception
             */
            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                System.out.println("Connection closed!");
            }

            @Override
            public boolean supportsPartialMessages() {
                return false;
            }
        };

        client.doHandshake(handler, "ws://localhost:5710/api/ws/MyToken");  // 连接到服务端的WebSocket端点
    }
}
