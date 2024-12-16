package com.wechat.ferry;

import com.wechat.ferry.plugin.PluginManager;
import com.wechat.ferry.socket.handler.WebSocketClientService;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import top.ruojy.chatbot.ChatBotPlugin;

import javax.annotation.Resource;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Set;

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
    private ApplicationContext applicationContext;

    @Resource
    private WebSocketClientService webSocketClientService;
    @Resource
    private PluginManager pluginManager;

    public static void main(String[] args) {
        SpringApplication.run(WeChatFerryApplication.class, args);
    }

    @Override
    public void run(String... args){
        webSocketClientService.startClient();
        // 插件所在目录
        File pluginDirectory = new File("src/main/resources/plugins");

        if (pluginDirectory.exists() && pluginDirectory.isDirectory()) {
            // 获取插件文件夹中的所有 JAR 文件
            File[] pluginFiles = pluginDirectory.listFiles((dir, name) -> name.endsWith(".jar"));

            if (pluginFiles != null) {
                for (File pluginFile : pluginFiles) {
                    try {
                        // 动态加载插件 JAR 文件
                        URL[] urls = { pluginFile.toURI().toURL() };
                        URLClassLoader classLoader = new URLClassLoader(urls, getClass().getClassLoader());

                        // 使用 Reflections 库扫描插件 JAR 中所有实现了 ChatBotPlugin 接口的类
                        Reflections reflections = new Reflections(
                                new org.reflections.util.ConfigurationBuilder()
                                        .setUrls(urls)  // 设置 URL，即 JAR 文件路径
                                        .setScanners(new SubTypesScanner(false))  // 扫描子类型
                                        .addClassLoaders(classLoader)  // 设置类加载器
                        );

                        // 获取所有实现了 ChatBotPlugin 接口的类
                        Set<Class<? extends ChatBotPlugin>> pluginClasses = reflections.getSubTypesOf(ChatBotPlugin.class);

                        // 遍历所有实现了 ChatBotPlugin 接口的类
                        for (Class<? extends ChatBotPlugin> pluginClass : pluginClasses) {
                            // 创建插件实例
                            ChatBotPlugin plugin = pluginClass.getDeclaredConstructor().newInstance();
                            pluginManager.initializePlugins(List.of(plugin));
                            // 将插件注册为 Spring Bean
                            registerPluginBean(plugin);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 注册插件为 Spring Bean
     */
    private void registerPluginBean(ChatBotPlugin plugin) {
        // 获取 BeanFactory
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();

        // 使用 AutowireCapableBeanFactory 将插件注册为 Bean
        beanFactory.registerSingleton(plugin.getClass().getSimpleName(), plugin);
    }
}
