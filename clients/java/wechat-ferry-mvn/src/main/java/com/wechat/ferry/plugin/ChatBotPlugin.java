package com.wechat.ferry.plugin;


import com.wechat.ferry.entity.dto.WxPpMsgDTO;
import com.wechat.ferry.entity.vo.MessageRequest;

public interface ChatBotPlugin {

    /**
     * 获取模块的优先级，优先级越高，数值越大
     * 默认为1
     */
    default int getPriority(){
        return 1;
    }

    /**
     * 获取开发者名
     */
    String getDeveloperName();

    /**
     * 获取模块版本
     */
    String getPluginVersion();

    /**
     * 模块描述
     * 请插件开发者自行重写
     */
    default String getPluginDescription(){
        return "作者比较懒，暂无描述";
    }

    /**
     * 插件初始化
     */
    void initialize();

    /**
     * 插件销毁
     */
    void destroy();

    /**
     * 处理已开启群聊消息
     * @param wxPpMsgDTO 消息内容
     */
    default void handleGroupMessage(WxPpMsgDTO wxPpMsgDTO){}

    /**
     * 处理已开启群聊艾特我消息
     * @param wxPpMsgDTO 消息内容
     */
    default void handleGroupAtMeMessage(WxPpMsgDTO wxPpMsgDTO){}

    /**
     * 处理个人消息
     * @param wxPpMsgDTO 消息内容
     */
    default void handlePersonalMessage(WxPpMsgDTO wxPpMsgDTO){}

}
