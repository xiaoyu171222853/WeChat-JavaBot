package com.wechat.ferry.config;

import com.wechat.ferry.utils.MyWebSocketClient;
import org.java_websocket.client.WebSocketClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class WebSocketClientConfigurer {

    private final String wsServerUrl = "ws://127.0.0.1:5710/api/ws/okenMyToken";
    private final String TOKEN = "myToken";
    @Bean
    public WebSocketClient webSocketClient() {
        try {
            MyWebSocketClient webSocketClient =
                    new MyWebSocketClient(new URI(wsServerUrl),TOKEN);
            webSocketClient.connect();
            return webSocketClient;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

}

