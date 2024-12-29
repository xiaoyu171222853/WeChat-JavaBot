package com.wechat.ferry.plugin;

import com.alibaba.fastjson2.JSONObject;
import com.wechat.ferry.entity.PluginClass;
import com.wechat.ferry.entity.PluginPackage;
import com.wechat.ferry.utils.HttpClientUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import top.ruojy.wxbot.ChatBotPlugin;

import javax.annotation.Resource;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.stream.Stream;

@Log4j2
@Component
public class PluginLoader {

    private final ApplicationContext applicationContext;
    @Resource
    private PluginManager pluginManager;

    // 通过构造函数注入 ApplicationContext
    @Autowired
    public PluginLoader(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public static final List<PluginPackage> pluginPackages = new ArrayList<>();

    // 插件加载方法
    public void loadPlugins() {
        // 插件所在目录
        File pluginDirectory = new File("config/plugins");
        if (!pluginDirectory.exists() || !pluginDirectory.isDirectory()) {
            log.error("插件路径不存在");
            return;
        }
        // 获取插件文件夹中的所有 JAR 文件
        File[] pluginFiles = pluginDirectory.listFiles((dir, name) -> name.endsWith(".jar"));

        if (pluginFiles != null) {
            for (File pluginFile : pluginFiles) {
                try {
                    // 动态加载插件 JAR 文件
                    URL[] urls = {pluginFile.toURI().toURL()};

                    // 使用 AnnotationConfigApplicationContext 注册插件中的 Bean
                    AnnotationConfigApplicationContext pluginContext = new AnnotationConfigApplicationContext();

                    URLClassLoader classLoader = new URLClassLoader(urls, applicationContext.getClassLoader());
                    pluginContext.setClassLoader(classLoader);
                    pluginContext.setParent(applicationContext);  // 使插件上下文成为主上下文的子上下文
                    List<String> remotePluginGroupIds = getRemotePluginGroupIdS();
                    // 转为String[]
                    String[] scanPackages = remotePluginGroupIds.toArray(new String[0]);
                    pluginContext.scan(scanPackages);

                    pluginContext.refresh();

                    // 将插件的 Bean 注册到主容器中
                    registerPluginBeans(pluginContext);

                    // 创建插件主类实例
                    Stream<ChatBotPlugin> chatBotPluginStream = pluginContext.getBeansOfType(ChatBotPlugin.class)
                            .values()
                            .stream();
                    chatBotPluginStream.forEach(plugin -> {

                        pluginManager.initializePlugins(Collections.singletonList(plugin));

                        // 获取插件的描述、版本、作者
                        String pluginName = plugin.getClass().getSimpleName();
                        String pluginDescription = plugin.getPluginDescription();
                        String pluginVersion = plugin.getPluginVersion();
                        String pluginAuthor = plugin.getDeveloperName();

                        // 构建 PluginClass 对象
                        PluginClass pluginClassObj = new PluginClass(pluginName, pluginDescription, pluginVersion, pluginAuthor);

                        // 构建 PluginPackage 对象
                        PluginPackage pluginPackage = new PluginPackage(pluginFile.getName(), Arrays.asList(pluginClassObj));
                        pluginPackages.add(pluginPackage);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            log.info("插件加载完成！");
        }else {
            log.info("未找到插件");
        }
    }

    /**
     * 注册插件为 Spring Bean
     */
    private void registerPluginBeans(AnnotationConfigApplicationContext pluginContext) {
        // 将插件中的 Bean 注册到主应用上下文中
        String[] beanNames = pluginContext.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            // 使用 Spring BeanFactory 将插件的 Bean 注册到主容器
            Object pluginBean = pluginContext.getBean(beanName);
            log.info("注册插件Bean: " + beanName);
            pluginContext.getAutowireCapableBeanFactory().autowireBean(pluginBean);
        }
    }
    /**
     * 获取插件GroupId
     */
    public static String getPluginGroupId(String pluginName) {
        for (PluginPackage pluginPackage : pluginPackages) {
            if (pluginPackage.getPackageName().equals(pluginName)) {
                return pluginPackage.getPackageName();
            }
        }
        return null;
    }
    /**
     * 远程获取插件GroupIdS
     */
    public static List<String> getRemotePluginGroupIdS() {
        String s = HttpClientUtil.doGet("https://www.ruojy.top/api/wx_bot_plugin/findAllGroupIds");
        JSONObject json = JSONObject.parseObject(s);
        return (List<String>)json.get("data");
    }
}
