package com.wechat.ferry;

import com.wechat.ferry.swing.ConfigWindow;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.io.File;

/**
 * 启动类
 *
 * @author chandler
 * @date 2024-09-21 12:19
 */
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.wechat.ferry"})
public class WeChatFerryApplication {

    public static void main(String[] args) {
        // Ensure the application is not running in headless mode
        System.setProperty("java.awt.headless", "false");
        // 在 Spring Boot 启动时显示配置窗口
        File configFile = new File("config/application.yaml");

        // 定义回调，点击“继续”后启动 Spring Boot
        Runnable onContinue = () -> {
            System.out.println("Spring Boot 应用启动...");
            // 你可以在这里调用 Spring Boot 启动的代码，或根据需要进行其他操作
            SpringApplication.run(WeChatFerryApplication.class, args); // 启动 Spring Boot 应用
        };

        // 创建并显示配置窗口
        new ConfigWindow(configFile, onContinue);
    }
}
