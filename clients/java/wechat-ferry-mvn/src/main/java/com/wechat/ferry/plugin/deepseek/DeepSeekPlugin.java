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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.Queue;

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
        // 创建一个临时的等待消息
        WxPpWcfSendTextMsgReq waitingMsg = new WxPpWcfSendTextMsgReq();
        waitingMsg.setMsgText("正在思考中...");
        waitingMsg.setRecipient(wxPpMsgDTO.getRoomId());
        waitingMsg.setAtUsers(Arrays.asList(wxPpMsgDTO.getSender()));
        waitingMsg.setIsAtAll(false);

        // 将等待消息加入队列
        messageQueue.offer(MessageRequest.create(MessageTypeEnum.TEXT, waitingMsg));

        // 异步处理消息
        deepSeekChatService.chat(wxPpMsgDTO.getRoomId(), wxPpMsgDTO.getRoomId(), wxPpMsgDTO.getContent(), deepSeekChatModel).thenAccept(response -> {
            // 创建实际的回复消息
            WxPpWcfSendTextMsgReq replyMsg = new WxPpWcfSendTextMsgReq();
            replyMsg.setMsgText(response.getContent());
            replyMsg.setRecipient(wxPpMsgDTO.getRoomId());
            replyMsg.setAtUsers(Arrays.asList(wxPpMsgDTO.getSender()));
            replyMsg.setIsAtAll(false);

            // 将响应消息加入队列
            messageQueue.offer(MessageRequest.create(MessageTypeEnum.TEXT, replyMsg));
        }).exceptionally(e -> {
            log.error("处理群聊@消息失败", e);
            // 发送错误提示
            WxPpWcfSendTextMsgReq errorMsg = new WxPpWcfSendTextMsgReq();
            errorMsg.setMsgText("抱歉，处理消息时出现错误，请联系小宇或稍后重试");
            errorMsg.setRecipient(wxPpMsgDTO.getRoomId());
            errorMsg.setAtUsers(Arrays.asList(wxPpMsgDTO.getSender()));
            errorMsg.setIsAtAll(false);

            messageQueue.offer(MessageRequest.create(MessageTypeEnum.TEXT, errorMsg));
            return null;
        });
    }

    @Override
    public void handlePersonalMessage(WxPpMsgDTO wxPpMsgDTO) {
        if (!this.filterXml(wxPpMsgDTO)){
            WxPpWcfSendTextMsgReq errorMsg = new WxPpWcfSendTextMsgReq();
            errorMsg.setMsgText("暂不支持该消息类型");
            errorMsg.setRecipient(wxPpMsgDTO.getSender());
            errorMsg.setIsAtAll(false);
            messageQueue.offer(MessageRequest.create(MessageTypeEnum.TEXT, errorMsg));
            return;
        }

        if (wxPpMsgDTO.getType().equals(10000)){
            WxPpWcfSendTextMsgReq likeMsg = new WxPpWcfSendTextMsgReq();
            likeMsg.setMsgText("我叫若小智，感谢您的喜欢");
            likeMsg.setRecipient(wxPpMsgDTO.getSender());
            likeMsg.setIsAtAll(false);
            messageQueue.offer(MessageRequest.create(MessageTypeEnum.TEXT, likeMsg));
            return;
        }

        if (wxPpMsgDTO.getContent().contains("#system")){
            String content = wxPpMsgDTO.getContent();
            // realContent = 你是一个中英文翻译专……
            String realContent = content.substring(content.indexOf(" ") + 1);
            // 添加系统消息
            deepSeekChatService.addSystem(wxPpMsgDTO.getSender(),realContent);
            WxPpWcfSendTextMsgReq successMsg = new WxPpWcfSendTextMsgReq();
            successMsg.setMsgText("添加成功");
            successMsg.setRecipient(wxPpMsgDTO.getSender());
            successMsg.setIsAtAll(false);
            messageQueue.offer(MessageRequest.create(MessageTypeEnum.TEXT, successMsg));
            return;
        }
        // 清除记忆
        if (wxPpMsgDTO.getContent().contains("#clear")){
            deepSeekChatService.clear(wxPpMsgDTO.getSender());
            WxPpWcfSendTextMsgReq clearMsg = new WxPpWcfSendTextMsgReq();
            clearMsg.setMsgText("清除成功");
            clearMsg.setRecipient(wxPpMsgDTO.getSender());
            clearMsg.setIsAtAll(false);
            messageQueue.offer(MessageRequest.create(MessageTypeEnum.TEXT, clearMsg));
            return;
        }

        // 创建一个临时的等待消息
        WxPpWcfSendTextMsgReq waitingMsg = new WxPpWcfSendTextMsgReq();
        waitingMsg.setMsgText("正在思考中...");
        waitingMsg.setRecipient(wxPpMsgDTO.getSender());
        waitingMsg.setIsAtAll(false);

        // 将等待消息加入队列
        messageQueue.offer(MessageRequest.create(MessageTypeEnum.TEXT, waitingMsg));

        // 异步处理消息
        deepSeekChatService.chat(wxPpMsgDTO.getSender(), wxPpMsgDTO.getSender(), wxPpMsgDTO.getContent(), deepSeekChatModel).thenAccept(response -> {
            // 创建实际的回复消息
            WxPpWcfSendTextMsgReq replyMsg = new WxPpWcfSendTextMsgReq();
            replyMsg.setMsgText(response.getContent());
            replyMsg.setRecipient(wxPpMsgDTO.getSender());
            replyMsg.setIsAtAll(false);

            // 将响应消息加入队列
            messageQueue.offer(MessageRequest.create(MessageTypeEnum.TEXT, replyMsg));
        }).exceptionally(e -> {
            log.error("处理私聊消息失败", e);
            WxPpWcfSendTextMsgReq errorMsg = new WxPpWcfSendTextMsgReq();
            errorMsg.setMsgText("抱歉，处理消息时出现错误，请联系小宇或稍后重试");
            errorMsg.setRecipient(wxPpMsgDTO.getSender());
            errorMsg.setIsAtAll(false);

            messageQueue.offer(MessageRequest.create(MessageTypeEnum.TEXT, errorMsg));
            return null;
        });
    }

    List<String> supportType = Arrays.asList(WcfMsgTypeEnum.TEXT.getCode(),
            WcfMsgTypeEnum.RED_ENVELOPE_SYS_NOTICE.getCode());

    /**
     * 是否支持该类型消息
     * @param wxPpMsgDTO wxPpMsgDTO
     * @return boolean
     */
    private boolean filterXml(WxPpMsgDTO wxPpMsgDTO) {
        return supportType.contains(wxPpMsgDTO.getType().toString());
    }

}
