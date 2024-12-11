package com.wechat.ferry;

import com.wechat.ferry.handle.ruojy.WebSocketClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类
 *
 * @author chandler
 * @date 2024-09-21 12:19
 */
@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.wechat.ferry")
public class WeChatFerryApplication implements CommandLineRunner {
    @Autowired
    private WebSocketClientService webSocketClientService;
    public static void main(String[] args) {
        SpringApplication.run(WeChatFerryApplication.class, args);

    }
    @Override
    public void run(String... args) {
        webSocketClientService.startClient();
    }
}
