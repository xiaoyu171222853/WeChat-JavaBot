package com.wechat.ferry.plugin.deepseek.store;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户仓库
 */
@Component
public class UserStore {

    // 用户id 聊天id列表
    public Map<Object, List<Object>> userMap = new HashMap<>();
    /**
     * 新增User
     * @param userId 用户id
     */
    public void addUser(Object userId) {
        userMap.put(userId, new ArrayList<>());
    }

    /**
     * 删除User
     * @param userId userId
     */
    public void delUser(Object userId) {
        userMap.remove(userId);
    }

    /**
     * 更新信息
     * @param userId userId
     * @param chatId chatId
     */
    public void updateUser(Object userId, Object chatId){
        List<Object> chatIdList = userMap.get(userId);
        chatIdList.add(chatId);
        userMap.replace(userId,chatIdList);
    }

    /**
     * 获取 用户的 聊天id 列表
     * @param userId userId
     * @return chatIdList
     */
    public List<Object> getChatIdList(Object userId){
        return userMap.get(userId);
    }

    /**
     * 判断是否有该用户
     * @param var1 userId
     * @return boolean
     */
    public boolean containsKey(Object var1) {
        return userMap.containsKey(var1);
    }

}
