package com.wechat.ferry.plugin.deepseek;

import com.wechat.ferry.entity.dto.WxPpMsgDTO;
import com.wechat.ferry.entity.vo.MessageRequest;
import com.wechat.ferry.entity.vo.request.WxPpWcfSendTextMsgReq;
import com.wechat.ferry.enums.MessageTypeEnum;
import com.wechat.ferry.enums.WcfMsgTypeEnum;
import com.wechat.ferry.enums.WxPpMsgTypeEnum;
import com.wechat.ferry.plugin.ChatBotPlugin;
import com.wechat.ferry.plugin.deepseek.service.DeepSeekChatService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import top.aoyudi.deepseek.entity.model.DeepSeekChatModel;
import top.aoyudi.deepseek.entity.request.message.DSSystemReqMessage;
import top.aoyudi.deepseek.entity.response.message.DSRespMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
@ComponentScan({"top.aoyudi"})
public class DeepSeekPlugin implements ChatBotPlugin {

    private static final Logger log = LoggerFactory.getLogger(DeepSeekPlugin.class);

    @Resource
    private DeepSeekChatService deepSeekChatService;
    @Resource
    private DeepSeekChatModel deepSeekChatModel;
    @Resource
    private Queue<MessageRequest> messageQueue;

    @Override
    public int getPriority() {
        return ChatBotPlugin.super.getPriority();
    }

    @Override
    public String getDeveloperName() {
        return "小宇";
    }

    @Override
    public String getPluginVersion() {
        return "1.0.0";
    }

    @Override
    public String getPluginDescription() {
        return "对接deepseek";
    }

    @Override
    public void initialize() {
        System.out.println("deepseek对接插件初始化");
    }

    @Override
    public void destroy() {
        System.out.println("deepseek对接插件被销毁");
    }

    @Override
    public void handleGroupAtMeMessage(WxPpMsgDTO wxPpMsgDTO) {
        if (this.handlerMsg(wxPpMsgDTO)){
            return;
        }
        // 异步处理消息
        deepSeekChatService.chat(wxPpMsgDTO.getRoomId(), wxPpMsgDTO.getContent(), deepSeekChatModel).thenAccept(response -> {
            // 创建实际的回复消息
            this.sendTestMessageWithAt(wxPpMsgDTO.getRoomId(), response.getContent());
        }).exceptionally(e -> {
            log.error("处理群聊@消息失败", e);
            // 发送错误提示
            this.sendTestMessageWithAt(wxPpMsgDTO.getRoomId(), "抱歉，处理消息时出现错误，请联系小宇或稍后重试");
            return null;
        });
    }

    @Override
    public void handlePersonalMessage(WxPpMsgDTO wxPpMsgDTO) {
        if (this.handlerMsg(wxPpMsgDTO)){
            return;
        }
        // 异步处理消息
        deepSeekChatService.chat(wxPpMsgDTO.getSender(), wxPpMsgDTO.getContent(), deepSeekChatModel).thenAccept(response -> {
            // 创建实际的回复消息
            this.sendTestMessage(wxPpMsgDTO.getSender(), response.getContent());
        }).exceptionally(e -> {
            log.error("处理私聊消息失败", e);
            this.sendTestMessage(wxPpMsgDTO.getSender(), "抱歉，处理消息时出现错误，请联系小宇或稍后重试");
            return null;
        });
    }

    /**
     * 发送文本消息
     * @param recipient 接收人
     * @param text 消息内容
     */
    private void sendTestMessageWithAt(String recipient, String text) {
        WxPpWcfSendTextMsgReq msg = new WxPpWcfSendTextMsgReq();
        msg.setMsgText(text);
        msg.setRecipient(recipient);
        msg.setAtUsers(Collections.singletonList(recipient));
        msg.setIsAtAll(false);
        messageQueue.offer(MessageRequest.create(MessageTypeEnum.TEXT, msg));
    }

    /**
     * 发送文本消息
     * @param recipient 接收人
     * @param text 消息内容
     */
    private void sendTestMessage(String recipient, String text) {
        WxPpWcfSendTextMsgReq msg = new WxPpWcfSendTextMsgReq();
        msg.setMsgText(text);
        msg.setRecipient(recipient);
        msg.setIsAtAll(false);
        messageQueue.offer(MessageRequest.create(MessageTypeEnum.TEXT, msg));
    }

    List<String> supportType = Arrays.asList(WcfMsgTypeEnum.TEXT.getCode(),
            WcfMsgTypeEnum.RED_ENVELOPE_SYS_NOTICE.getCode());

    /**
     * 是否支持该类型消息
     * @param wxPpMsgDTO 微信消息
     * @return boolean
     */
    private boolean filterXml(WxPpMsgDTO wxPpMsgDTO) {
        return supportType.contains(wxPpMsgDTO.getType().toString());
    }

    /**
     * 处理消息
     * @param wxPpMsgDTO 微信消息
     * @return boolean
     */
    private boolean handlerMsg(WxPpMsgDTO wxPpMsgDTO){
        List<Function<WxPpMsgDTO, Boolean>> handlers = Arrays.asList(
                this::handlerMsgType,
                this::handlerFollowMsg,
                this::handlerSystemMsg,
                this::handlerClearMsg
        );

        return handlers.stream().anyMatch(handler -> handler.apply(wxPpMsgDTO));
    }

    /**
     * 处理消息类型
     * @param wxPpMsgDTO 微信消息
     * @return boolean
     */
    public boolean handlerMsgType(WxPpMsgDTO wxPpMsgDTO){
        if (!this.filterXml(wxPpMsgDTO)){
            this.sendTestMessage(wxPpMsgDTO.getSender(), "暂不支持该消息类型");
            return true;
        }
        return false;
    }

    /**
     * 处理添加消息
     * @param wxPpMsgDTO 微信消息
     * @return boolean
     */
    public boolean handlerFollowMsg(WxPpMsgDTO wxPpMsgDTO){
        if (wxPpMsgDTO.getType().equals(10000)){
            this.sendTestMessage(wxPpMsgDTO.getSender(), "我叫若小智,感谢您的喜欢");
            return true;
        }
        return false;
    }

    /**
     * 处理添加system
     * @param wxPpMsgDTO 微信消息
     * @return boolean
     */
    private boolean handlerSystemMsg(WxPpMsgDTO wxPpMsgDTO){
        if (wxPpMsgDTO.getContent().contains("#system")||wxPpMsgDTO.getContent().contains("＃system")){
            String content = wxPpMsgDTO.getContent();
            // realContent = 你是一个中英文翻译专……
            String realContent = content.substring(content.indexOf(" ") + 1);
            // 添加系统消息
            deepSeekChatService.addSystem(wxPpMsgDTO.getSender(),realContent);
            this.sendTestMessage(wxPpMsgDTO.getSender(), "添加成功");
            return true;
        }
        return false;
    }

    /**
     * 处理清除记忆
     * @param wxPpMsgDTO 微信消息
     * @return boolean
     */
    public boolean handlerClearMsg(WxPpMsgDTO wxPpMsgDTO){
        // 清除记忆
        if (wxPpMsgDTO.getContent().contains("#clear")||wxPpMsgDTO.getContent().contains("＃clear")){
            deepSeekChatService.clear(wxPpMsgDTO.getSender());
            this.sendTestMessage(wxPpMsgDTO.getSender(), "清除成功");
            return true;
        }
        return false;
    }

}
