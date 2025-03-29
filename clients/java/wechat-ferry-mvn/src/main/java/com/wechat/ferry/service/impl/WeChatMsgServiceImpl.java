package com.wechat.ferry.service.impl;

import java.util.*;

import javax.annotation.Resource;

import com.wechat.ferry.plugin.PluginManager;
import com.wechat.ferry.plugin.PluginManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.wechat.ferry.config.WeChatFerryProperties;
import com.wechat.ferry.service.WeChatMsgService;
import com.wechat.ferry.utils.HttpClientUtil;

import lombok.extern.slf4j.Slf4j;
import com.wechat.ferry.entity.dto.WxPpMsgDTO;
import com.wechat.ferry.service.WeChatDllService;

/**
 * 业务实现层-消息处理
 *
 * @author chandler
 * @date 2024-10-01 14:35
 */
@Slf4j
@Service
public class WeChatMsgServiceImpl implements WeChatMsgService {

    @Resource
    private WeChatFerryProperties weChatFerryProperties;
    @Resource
    private WeChatDllService weChatDllService;

    @Resource
    private PluginManager pluginManager;

    private static final List<String> functions = new ArrayList<>(Collections.singletonList("菜单"));

    @Override
    public void receiveMsg(String jsonString) {
        // 转发接口处理
        receiveMsgCallback(jsonString);
        // 转为JSON对象
        WxPpMsgDTO dto = JSON.parseObject(jsonString, WxPpMsgDTO.class);
        // 群聊消息处理
        if (dto.getIsGroup()) {
            // 判断是否有开启的群聊配置
            if (!CollectionUtils.isEmpty(weChatFerryProperties.getOpenMsgGroups())) {
                // 是否为开启的群聊
                if (weChatFerryProperties.getOpenMsgGroups().contains(dto.getRoomId())) {
                    // 判断是否有艾特我
                    if (weChatDllService.isAtMeMsg(dto.getXml(), dto.getContent())){
                        log.info("[收到消息]-[开启群聊]-[收到艾特我]-打印：{}\n", dto.getContent());
                        log.info("消息内容:{}", dto.getContent());
                        // 去除艾特部分
                        // 寻找艾特人与消息内容之间分隔符位置
                        int spaceIndex = dto.getContent().indexOf(" ");
                        // 从分隔符后面开始提取
                        String content = dto.getContent().substring(spaceIndex + 1);
                        // 赋值
                        dto.setContent(content);
                        // 处理群聊艾特我消息
                        pluginManager.handleGroupAtMeMessage(dto);
                    }else {
                        log.info("[收到消息]-[开启群聊]-打印{}", dto.getContent());
                        // 处理框架内置功能（菜单）
                        if (functions.contains(dto.getContent())){
                            pluginManager.frameWorkFunction(dto);
                            return;
                        }
                        // 非本人发送的消息才进行处理
//                        if (!dto.getIsSelf()){
//                            pluginManager.handleGroupMessage(dto);
//                        }
                        // 调试时开启
//                        pluginManager.handleGroupMessage(dto);
                    }
                }else {
                    log.info("[收到消息]-[未开启群聊]：群聊id:{}，聊天内容:{}\n", dto.getRoomId(), dto.getContent());
                    return;
                }
            }else {
                return;
            }
        }
        // 判断是否为个人号 私聊消息处理
        if (dto.getRoomId().startsWith("wxid_")) {
            // 处理框架内置功能（菜单）
            if (functions.contains(dto.getContent())){
                pluginManager.frameWorkFunction(dto);
                return;
            }
            log.info("[收到消息]-[私聊]：{}\n", dto);
            // 非本人发送的消息才进行处理
            if (!dto.getIsSelf()) {
                pluginManager.handlePersonalMessage(dto);
            }
        }
    }

    private void receiveMsgCallback(String jsonString) {
        // 开启回调，且回调地址不为空
        if (weChatFerryProperties.getReceiveMsgCallbackSwitch() && !CollectionUtils.isEmpty(weChatFerryProperties.getReceiveMsgCallbackUrls())) {
            for (String receiveMsgFwdUrl : weChatFerryProperties.getReceiveMsgCallbackUrls()) {
                if (!receiveMsgFwdUrl.startsWith("http")) {
                    continue;
                }
                try {
                    String responseStr = HttpClientUtil.doPostJson(receiveMsgFwdUrl, jsonString);
                    if (judgeSuccess(responseStr)) {
                        log.error("[接收消息]-消息回调至外部接口,获取响应状态失败！-URL：{}", receiveMsgFwdUrl);
                    }
                    log.debug("[接收消息]-[回调接收到的消息]-回调消息至：{}", receiveMsgFwdUrl);
                } catch (Exception e) {
                    log.error("[接收消息]-消息回调接口[{}]服务异常：", receiveMsgFwdUrl, e);
                }
            }
        }
    }

    private Boolean judgeSuccess(String responseStr) {
        // 默认为通过
        boolean passFlag = false;
        if (!ObjectUtils.isEmpty(responseStr)) {
            JSONObject jSONObject = JSONObject.parseObject(responseStr);
            if (!ObjectUtils.isEmpty(jSONObject) && !CollectionUtils.isEmpty(weChatFerryProperties.getThirdPartyOkCodes())) {
                Map<String, String> codeMap = weChatFerryProperties.getThirdPartyOkCodes();
                for (Map.Entry<String, String> entry : codeMap.entrySet()) {
                    if (!ObjectUtils.isEmpty(jSONObject.get(entry.getKey())) && jSONObject.get(entry.getKey()).equals(entry.getValue())) {
                        passFlag = true;
                        break;
                    }
                }
            }
        }
        return passFlag;
    }

}
