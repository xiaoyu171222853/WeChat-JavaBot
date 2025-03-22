package com.wechat.ferry.plugin.deepseek.util;

import com.wechat.ferry.plugin.deepseek.store.DeepSeekMessageStore;
import com.wechat.ferry.plugin.deepseek.store.UserStore;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import top.aoyudi.deepseek.entity.request.message.DSSystemReqMessage;
import top.aoyudi.deepseek.entity.request.message.DSUserReqMessage;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * deepseek工具类
 */
@Log4j2
@Component
public class DeepSeekUtil {

    // 消息仓库
    @Resource
    private DeepSeekMessageStore deepSeekMessageStore;

    // 用户仓库
    @Resource
    private UserStore userStore;

    /**
     * 向deepseek发消息前奏
     * @param userId 用户Id
     * @param chatId 聊天Id
     * @param content 内容
     */
    public void preChat(String userId, String chatId, String content){
        // 判断是否有该用户
        if (!userStore.containsKey(userId)){
            userStore.addUser(userId);
        }
        // 判断是否有该消息
        if (!deepSeekMessageStore.containsKey(chatId)) {
            // 用户聊天列表更新
            userStore.updateUser(userId,chatId);
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
            userStore.addUser(userId);
            deepSeekMessageStore.addChat(userId);
            deepSeekMessageStore.updateMessages(userId,dsSystemReqMessage);
        });
        log.info("system添加成功");
    }
}
