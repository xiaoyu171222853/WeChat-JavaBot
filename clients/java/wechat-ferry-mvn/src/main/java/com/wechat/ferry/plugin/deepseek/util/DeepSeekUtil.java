package com.wechat.ferry.plugin.deepseek.util;

import com.wechat.ferry.plugin.deepseek.store.DeepSeekMessageStore;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.aoyudi.deepseek.entity.request.message.DSSystemReqMessage;
import top.aoyudi.deepseek.entity.request.message.DSUserReqMessage;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * deepseek工具类
 */
@Log4j2
@Component
public class DeepSeekUtil {

    // 消息仓库
    @Resource
    private DeepSeekMessageStore deepSeekMessageStore;

    /**
     * 向deepseek发消息前奏
     * @param chatId 聊天Id
     * @param content 内容
     */
    public void preChat(String chatId, String content){
        // 判断是否有该消息
        if (!deepSeekMessageStore.containsKey(chatId)) {
            deepSeekMessageStore.addChat(chatId);
        }
        // 用户消息
        DSUserReqMessage userDeepSeekMessage = new DSUserReqMessage();
        // 设置 用户消息内容
        userDeepSeekMessage.setContent(content);
        // 更新用户聊天信息
        deepSeekMessageStore.updateMessages(chatId,userDeepSeekMessage);
    }

    private static final List<String> userIdList = Arrays.asList();

    /**
     * 给用户列表添加指定system
     */
    @PostConstruct
    private void addDefaultSystem(){
        if (userIdList.isEmpty()){
            return;
        }
        DSSystemReqMessage dsSystemReqMessage = new DSSystemReqMessage();
        dsSystemReqMessage.setContent("请你扮演xxxxxxxxx。");

        userIdList.forEach(userId->{
            // 添加chat
            deepSeekMessageStore.addChat(userId);
            // 添加system
            deepSeekMessageStore.addSystem(userId,dsSystemReqMessage);
        });
        log.info("system添加成功");
    }

    /**
     * 清理超过3天没有的用户及记忆
     */
    @Scheduled(fixedRate = 86400000) // 每 24 小时执行一次
    private void clearOutTimeMessage(){
        LocalDateTime threeDaysAgo = LocalDateTime.now().minus(2, ChronoUnit.DAYS);
        Map<Object, LocalDateTime> lastUpdatedMap = deepSeekMessageStore.getLastUpdatedMap();
        for (Map.Entry<Object, LocalDateTime> entry : lastUpdatedMap.entrySet()) {
            Object chatId = entry.getKey();
            LocalDateTime lastUpdated = entry.getValue();

            // 判断是否超过 3 天
            if (lastUpdated.isBefore(threeDaysAgo)) {
                System.out.println("Chat " + chatId + " has not been updated for more than 3 days.");
                // 清理前进行存储
                deepSeekMessageStore.archiveOldChat(chatId);
                // 执行清理操作
                deepSeekMessageStore.delChat(chatId);
            }
        }
    }
}
