package com.wechat.ferry.plugin.deepseek.service;

import com.wechat.ferry.plugin.deepseek.store.DeepSeekMessageStore;
import com.wechat.ferry.plugin.deepseek.util.DeepSeekUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.aoyudi.chat.entity.ChatMessage;
import top.aoyudi.deepseek.entity.enums.DeepSeekRoleEnum;
import top.aoyudi.deepseek.entity.model.DeepSeekModel;
import top.aoyudi.deepseek.entity.request.DeepSeekRequest;
import top.aoyudi.deepseek.entity.request.message.DSSystemReqMessage;
import top.aoyudi.deepseek.entity.response.DSResponse;
import top.aoyudi.deepseek.entity.response.message.DSRespMessage;
import top.aoyudi.deepseek.service.DeepSeekService;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class DeepSeekChatService {
    @Resource
    private DeepSeekService deepSeekService;
    // 请求体
    private static final DeepSeekRequest request = new DeepSeekRequest();
    @Resource
    private DeepSeekUtil deepSeekUtil;
    // 消息仓库
    @Resource
    private DeepSeekMessageStore deepSeekMessageStore;

    /**
     * 添加 system
     * @param chatId chatId
     * @param content system内容
     */
    public void addSystem(String chatId,String content){
        // 添加一调system的消息
        DSSystemReqMessage dsSystemReqMessage = new DSSystemReqMessage();
        dsSystemReqMessage.setContent(content);
        deepSeekMessageStore.addSystem(chatId,dsSystemReqMessage);
    }

    public void clear(String chatId){
        deepSeekMessageStore.delChat(chatId);
    }

    @Async("deepSeekExecutor")
    public CompletableFuture<DSRespMessage> chat(String chatId, String content, DeepSeekModel deepSeekModel) {
        request.setDeepSeekModel(deepSeekModel);
        // 构建消息 更新仓库
        deepSeekUtil.preChat(chatId,content);

        // 请求体 设置 聊天 信息
        request.setMessages(deepSeekMessageStore.getMessages(chatId));
        DSResponse response = deepSeekService.sendMessage(request);
        // 处理响应
        if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
            // 获取内容
            DSRespMessage aiAssistDeepSeekMessage = response.getChoices().get(0).getMessage();
            System.out.println("响应："+aiAssistDeepSeekMessage.getReasoning_content());
            // 往仓库中放deepseek的响应
            ChatMessage chatMessage = new ChatMessage();
            BeanUtils.copyProperties(aiAssistDeepSeekMessage,chatMessage);
            // 更新
            deepSeekMessageStore.updateMessages(chatId,chatMessage);
            return CompletableFuture.completedFuture(aiAssistDeepSeekMessage);
        }
        DSRespMessage dsRespMessage = new DSRespMessage();
        dsRespMessage.setRole(DeepSeekRoleEnum.ASSISTANT.getRole());
        dsRespMessage.setContent("服务器繁忙，请稍后再试");
        return CompletableFuture.completedFuture(dsRespMessage);
    }
}
