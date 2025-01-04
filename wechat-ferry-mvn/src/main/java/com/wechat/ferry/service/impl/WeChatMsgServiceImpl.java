package com.wechat.ferry.service.impl;

import java.util.*;

import javax.annotation.Resource;

import com.wechat.ferry.plugin.PluginManager;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.wechat.ferry.config.WeChatFerryProperties;
import com.wechat.ferry.service.WeChatMsgService;
import com.wechat.ferry.utils.HttpClientUtil;

import lombok.extern.slf4j.Slf4j;
import top.ruojy.wxbot.entity.dto.WxPpMsgDTO;
import top.ruojy.wxbot.service.WeChatDllService;

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
        receiveMsgForward(jsonString);
        // 转为JSON对象
        WxPpMsgDTO dto = JSON.parseObject(jsonString, WxPpMsgDTO.class);
        log.info(dto.toString());
        // 群聊消息处理
        if (dto.getIsGroup()) {
            // 判断是否有开启的群聊配置
            if (!CollectionUtils.isEmpty(weChatFerryProperties.getOpenMsgGroups())) {
                // 是否为开启的群聊
                if (weChatFerryProperties.getOpenMsgGroups().contains(dto.getRoomId())) {
                    // 判断是否有艾特我
                    if (weChatDllService.isAtMeMsg(dto.getXml(), dto.getContent())){
                        log.info("[收到消息]-[收到艾特我]-打印：{}\n", dto);
                        log.info("消息内容:{}", dto.getContent());
                        pluginManager.handleGroupAtMeMessage(dto);
                    }else {
                        log.debug("[收到消息]-[开启群聊]-打印：{}", dto);
                        // 处理框架内置功能（菜单）
                        if (functions.contains(dto.getContent())){
                            pluginManager.frameWorkFunction(dto);
                            return;
                        }
                        // 非本人发送的消息才进行处理
                        if (!dto.getIsSelf()){
                            pluginManager.handleGroupMessage(dto);
                        }
                    }
                }else {
                    // [收到消息]-[未开启群聊]
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
            // 非本人发送的消息才进行处理
            if (!dto.getIsSelf()) {
                pluginManager.handlePersonalMessage(dto);
            }
        }
    }

    private void receiveMsgForward(String jsonString) {
        // 开启转发，且转发地址不为空
        if (weChatFerryProperties.getReceiveMsgFwdSwitch() && !CollectionUtils.isEmpty(weChatFerryProperties.getReceiveMsgFwdUrls())) {
            for (String receiveMsgFwdUrl : weChatFerryProperties.getReceiveMsgFwdUrls()) {
                if (!receiveMsgFwdUrl.startsWith("http")) {
                    continue;
                }
                try {
                    String responseStr = HttpClientUtil.doPostJson(receiveMsgFwdUrl, jsonString);
                    if (judgeSuccess(responseStr)) {
                        log.error("[接收消息]-消息转发外部接口,获取响应状态失败！-URL：{}", receiveMsgFwdUrl);
                    }
                    log.debug("[接收消息]-[转发接收到的消息]-转发消息至：{}", receiveMsgFwdUrl);
                } catch (Exception e) {
                    log.error("[接收消息]-消息转发接口[{}]服务异常：", receiveMsgFwdUrl, e);
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
