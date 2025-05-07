package com.wechat.ferry.plugin.dev;

import com.wechat.ferry.entity.dto.WxPpMsgDTO;
import com.wechat.ferry.entity.vo.MessageRequest;
import com.wechat.ferry.plugin.ChatBotPlugin;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;


@Log4j2
@Component
public class PluginDevMain implements ChatBotPlugin {

    @Override
    public int getPriority() {
        return ChatBotPlugin.super.getPriority();
    }

    /**
     * 开发者名
     * @return 开发者名
     */
    @Override
    public String getDeveloperName() {
        return "开发测试者";
    }

    /**
     * 插件版本
     * @return 插件版本
     */
    @Override
    public String getPluginVersion() {
        return "1.0.0";
    }

    /**
     * 插件描述
     * @return 插件描述
     */
    @Override
    public String getPluginDescription() {
        return "一个测试插件";
    }

    /**
     * 插件初始化调用
     */
    @Override
    public void initialize() {
        log.info("测试插件初始化");
    }

    /**
     * 插件被销毁调用
     */
    @Override
    public void destroy() {
        log.info("插件被销毁");
    }

    /**
     * 处理组群消息
     * @param wxPpMsgDTO wxPpMsgDTO
     * @return MessageRequest
     */
    @Override
    public void handleGroupMessage(WxPpMsgDTO wxPpMsgDTO) {
    }


    /**
     * 处理组群艾特我消息
     * @param wxPpMsgDTO wxPpMsgDTO
     * @return MessageRequest
     */
    @Override
    public void handleGroupAtMeMessage(WxPpMsgDTO wxPpMsgDTO) {
    }

    /**
     * 处理 私聊 消息
     * @param wxPpMsgDTO wxPpMsgDTO
     * @return MessageRequest
     */
    @Override
    public void handlePersonalMessage(WxPpMsgDTO wxPpMsgDTO) {
    }
}
