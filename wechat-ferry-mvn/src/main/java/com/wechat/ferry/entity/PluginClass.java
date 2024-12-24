package com.wechat.ferry.entity;

public class PluginClass {
    private String className;  // 类名
    private String description;  // 类的描述
    private String version;  // 类的版本
    private String author;  // 类的作者

    // 构造函数
    public PluginClass(String className, String description, String version, String author) {
        this.className = className;
        this.description = description;
        this.version = version;
        this.author = author;
    }

    // Getter 和 Setter 方法

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "PluginClass{" +
                "className='" + className + '\'' +
                ", description='" + description + '\'' +
                ", version='" + version + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
