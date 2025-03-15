package com.wechat.ferry.entity;

import java.util.List;

public class PluginPackage {
    private String packageName;  // 插件包名
    private List<PluginClass> classes;  // 包内的多个类

    // 构造函数
    public PluginPackage(String packageName, List<PluginClass> classes) {
        this.packageName = packageName;
        this.classes = classes;
    }

    // Getter 和 Setter 方法

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<PluginClass> getClasses() {
        return classes;
    }

    public void setClasses(List<PluginClass> classes) {
        this.classes = classes;
    }

    @Override
    public String toString() {
        return "PluginPackage{" +
                "packageName='" + packageName + '\'' +
                ", classes=" + classes +
                '}';
    }
}
