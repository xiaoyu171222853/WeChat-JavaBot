package com.wechat.ferry.entity;

import com.wechat.ferry.plugin.ChatBotPlugin;
import lombok.Data;

@Data
public class PluginClass {
    private String className;  // 类名
    private String description;  // 类的描述
    private String version;  // 类的版本
    private String author;  // 类的作者
    private ChatBotPlugin plugin; // 插件本体

    // 构造函数
    public PluginClass(String className, String description, String version, String author,ChatBotPlugin plugin) {
        this.className = className;
        this.description = description;
        this.version = version;
        this.author = author;
        this.plugin = plugin;
    }
}
