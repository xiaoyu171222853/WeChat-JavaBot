package com.wechat.ferry.plugin.deepseek.store;

import org.springframework.stereotype.Component;
import top.aoyudi.chat.entity.ChatMessage;
import top.aoyudi.deepseek.entity.enums.DeepSeekRoleEnum;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * deepseek记忆仓库
 */
@Component
public class DeepSeekMessageStore{
    // 聊天id 消息内容列表
    protected Map<Object, CopyOnWriteArrayList<ChatMessage>> messageMap = new ConcurrentHashMap<>();

    /**
     * 判断是否有key
     * @param var1 var1
     * @return boolean
     */
    public boolean containsKey(Object var1) {
        return messageMap.containsKey(var1);
    }
    public List<ChatMessage> getMessages(Object chatId) {
        return messageMap.get(chatId);
    }

    /**
     * 新增Chat Id
     * @param chatId 聊天id
     */
    public void addChat(Object chatId) {
        messageMap.put(chatId,new CopyOnWriteArrayList<>());
    }

    /**
     * 删除Chat
     * @param chatId chatId
     */
    public void delChat(Object chatId) {
        if (containsKey(chatId)) {
            messageMap.remove(chatId);
        }
    }

    /**
     * 修改messages
     * @param chatId
     * @param chatMessage
     */
    public void updateMessages(Object chatId, ChatMessage chatMessage) {
        messageMap.get(chatId).add(chatMessage);
    }

    /**
     *
     */
    public void addSystem(Object chatId, ChatMessage chatMessage){
        if (!this.containsKey(chatId)){
            this.addChat(chatId);
        }
        CopyOnWriteArrayList<ChatMessage> chatMessages = messageMap.get(chatId);
        if (chatMessages.isEmpty()){
            chatMessages.add(chatMessage);
            return;
        }
        // 查找system消息
        for (ChatMessage message : chatMessages) {
            if (message.role.equals(DeepSeekRoleEnum.SYSTEM.getRole())){
                // 删除原有system
                chatMessages.remove(message);
                break;
            }
        }
        chatMessages.add(chatMessage);
    }
}
