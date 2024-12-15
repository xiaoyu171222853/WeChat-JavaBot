package com.wechat.ferry.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.wechat.ferry.service.WeChatDllService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.wechat.ferry.config.WeChatFerryProperties;
import com.wechat.ferry.entity.dto.WxPpMsgDTO;
import com.wechat.ferry.service.WeChatMsgService;
import com.wechat.ferry.utils.HttpClientUtil;

import lombok.extern.slf4j.Slf4j;

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

    @Override
    public void receiveMsg(String jsonString) {
        // 转发接口处理
        receiveMsgForward(jsonString);
        // 转为JSON对象
        WxPpMsgDTO dto = JSON.parseObject(jsonString, WxPpMsgDTO.class);
        log.info(dto.toString());
        // 有开启的群聊配置
        if (!CollectionUtils.isEmpty(weChatFerryProperties.getOpenMsgGroups())) {
            // 指定处理的群聊
            if (weChatFerryProperties.getOpenMsgGroups().contains(dto.getRoomId())) {
                // 判断是否有艾特我
                if (weChatDllService.isAtMeMsg(dto.getXml(), dto.getContent())){
                    log.info("[收到消息]-[收到艾特我]-打印：{}\n", dto);
                    log.info("消息内容:{}", dto.getContent());

                }else {
                    log.debug("[收到消息]-[指定群聊]-打印：{}", dto);
                }
            }
        }else {

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
