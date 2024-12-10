package com.wechat.ferry.utils;

import com.alibaba.fastjson2.JSON;
import com.wechat.ferry.entity.Message;
import com.wechat.ferry.enums.MessageTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

@Slf4j
public class MyWebSocketClient extends WebSocketClient {

    private final String myToken;
    private final String serverUri;

    public MyWebSocketClient(URI serverUri, String myToken) {
        super(serverUri);
        this.serverUri = serverUri.toString();
        this.myToken = myToken;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        log.info("------ WebSocketClient onOpen ------");
    }

    @Override
    public void onMessage(String messageStr) {
        log.info("接收到服务端消息：{}", messageStr);
        // 解析消息
        Message message = parseMessage(messageStr);

        if (message != null) {
            // 根据消息类型进行处理
            switch (message.getType()) {
                case HEARTBEAT:
                    sendMessage(new Message(MessageTypeEnum.HEARTBEAT,"PONG",myToken,serverUri));
                    break;
                case TEXT:
                    System.out.println("收到文本消息: " + message.getContent());
                    break;
                case NOTIFICATION:
                    System.out.println("通知: " + message.getContent());
                    break;
                default:
                    System.out.println("未知消息类型: " + message.getType());
            }
        }
    }
    private Message parseMessage(String messageStr) {
        // 假设接收到的消息是 JSON 格式
        // 解析消息并返回 Message 对象
        return JSON.parseObject(messageStr, Message.class);
    }
    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info("------ WebSocket onClose ------{}", reason);
    }

    @Override
    public void onError(Exception ex) {
        log.error("------ WebSocket onError ------{}", ex.getMessage());
    }

    private void sendMessage(Message message) {
        send(message.toJson());
    }
}
