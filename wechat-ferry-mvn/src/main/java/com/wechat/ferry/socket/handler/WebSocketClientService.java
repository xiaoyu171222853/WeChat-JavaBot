package com.wechat.ferry.socket.handler;


import com.alibaba.fastjson2.JSON;
import com.wechat.ferry.socket.entity.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.wechat.ferry.socket.enums.RMessageTypeEnum.HEARTBEAT;

/**
 * websocket客户端服务
 */
@Component
public class WebSocketClientService {

    @Value("${ruojy.token}")
    private String myToken;

    @Value("${ruojy.wsUrl}")
    private String wsUrl;

    @Resource
    private RRequestMessageProcessor RRequestMessageProcessor;

    private WebSocketSession currentSession;
    private WebSocketClient client;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);  // 定时任务线程池

    private boolean isConnected = false;  // 连接状态标记
    private boolean isConnectionAttempting = false;  // 标记是否正在尝试连接

    private static WebSocketHandler handler = null;
    // 以配置文件为主
    // 尝试重连间隔时间
    @Value("${ruojy.reConnectToServerTimeout}")
    private final Integer reConnectToServerTimeout = 30;
    // 超时断开重试时间
    @Value("${ruojy.overTimeout}")
    private final Integer overTimeout = 20;
    public void startClient() {
        client = new StandardWebSocketClient();
        handler = new WebSocketHandler() {

            @Override
            public void afterConnectionEstablished(WebSocketSession session) {
                System.out.println("Connected to server!");
                currentSession = session;
                isConnected = true;  // 连接成功，设置连接状态
                isConnectionAttempting = false;  // 停止连接尝试
            }

            @Override
            public void handleMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) throws IOException {
                System.out.println("Received from server: " + webSocketMessage.getPayload());
                Message message = Message.parseMessage((String) webSocketMessage.getPayload());

                if (message != null) {
                    switch (message.getType()) {
                        case HEARTBEAT:
                            sendMessage(session, new Message(HEARTBEAT, "PONG", myToken, message.getRequestId()));
                            break;
                        case REQUEST:
                            RRequestMessageProcessor.processRequest(message, session);
                            break;
                        case NOTIFICATION:
                            System.out.println("通知: " + message.getContent());
                            break;
                        default:
                            System.out.println("未知消息类型: " + message.getType());
                    }
                }
            }

            private void sendMessage(WebSocketSession session, Message message) throws IOException {
                session.sendMessage(new TextMessage(JSON.toJSONString(message)));
            }

            @Override
            public void handleTransportError(WebSocketSession session, Throwable exception) {
                System.err.println("Transport error: " + exception.getMessage());
                exception.printStackTrace();
                isConnected = false;
                isConnectionAttempting = false;
                reconnect();
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                System.out.println("Connection closed with status: " + closeStatus);
                isConnected = false;
                isConnectionAttempting = false;
                reconnect();
            }

            @Override
            public boolean supportsPartialMessages() {
                return false;
            }
        };

        // 尝试连接
        connectToServer();
    }

    /**
     * 连接到WebSocket服务器
     */
    private void connectToServer() {
        if (isConnectionAttempting) {
            return;  // 如果已经在尝试连接，跳过
        }

        isConnectionAttempting = true;  // 设置连接状态为正在连接
        System.out.println("Attempting to connect to WebSocket: " + wsUrl + "/" + myToken);

        // 设置20秒超时，如果超过20秒还没有成功连接，认为超时
        scheduler.schedule(() -> {
            if (!isConnected) {
                System.out.println("Connection attempt timed out after "+overTimeout+" seconds.");
                isConnectionAttempting = false;  // 取消连接尝试
                reconnect();  // 30秒后重新连接
            }
        }, overTimeout, TimeUnit.SECONDS);  // 默认20秒超时任务

        // 连接到WebSocket服务器
        client.doHandshake(handler, wsUrl + "/" + myToken);
    }

    /**
     * 超时后重新尝试连接
     */
    private void reconnect() {
        if (!isConnected) {
            System.out.println("Reconnecting in "+reConnectToServerTimeout+" seconds...");
            scheduler.schedule(this::connectToServer, reConnectToServerTimeout, TimeUnit.SECONDS);  // 默认30秒后重试连接
        }
    }
}
