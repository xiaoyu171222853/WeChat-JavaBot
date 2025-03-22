package com.wechat.ferry.plugin;

import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
class PluginDevTest implements ChatBotPlugin {

    @Override
    public String getDeveloperName() {
        return "";
    }

    @Override
    public String getPluginVersion() {
        return "";
    }

    @Override
    public void initialize() {

    }

    @Override
    public void destroy() {

    }
}
