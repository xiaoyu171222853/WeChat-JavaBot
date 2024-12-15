package com.wechat.ruojy.handler.methodHandler;

import com.alibaba.fastjson2.JSON;
import com.wechat.ferry.entity.vo.response.WxPpWcfContactsResp;
import com.wechat.ferry.service.WeChatDllService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class GetContactsListHandler implements MethodHandler{

    @Resource
    private WeChatDllService weChatDllService;

    @Override
    public String getMethodName() {
        return "getContactsList";
    }

    @Override
    public Object handle(Map<String, Object> params) {
        String json = "[{\n" +
                "      \"weChatUid\": \"wxid_tl9x147gnxvz22\",\n" +
                "      \"weChatNo\": \"nymcloud\",\n" +
                "      \"weChatNickname\": \"祓\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": null,\n" +
                "      \"countryPinyin\": \"FI\",\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": null,\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": null,\n" +
                "      \"sex\": \"1\",\n" +
                "      \"sexLabel\": \"男\",\n" +
                "      \"type\": \"1\",\n" +
                "      \"typeLabel\": \"个微\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"wxid_hyoeeugq38jh22\",\n" +
                "      \"weChatNo\": \"x3559521032\",\n" +
                "      \"weChatNickname\": \" confuse\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": \"中国\",\n" +
                "      \"countryPinyin\": \"CN\",\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": \"Shanxi\",\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": \"Linfen\",\n" +
                "      \"sex\": \"1\",\n" +
                "      \"sexLabel\": \"男\",\n" +
                "      \"type\": \"1\",\n" +
                "      \"typeLabel\": \"个微\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"qqmail\",\n" +
                "      \"weChatNo\": \"\",\n" +
                "      \"weChatNickname\": \"QQ邮箱提醒\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": null,\n" +
                "      \"countryPinyin\": null,\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": null,\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": null,\n" +
                "      \"sex\": \"0\",\n" +
                "      \"sexLabel\": \"未知\",\n" +
                "      \"type\": \"1\",\n" +
                "      \"typeLabel\": \"个微\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"gh_6fe6b4b86d82\",\n" +
                "      \"weChatNo\": \"tmdjwkj\",\n" +
                "      \"weChatNickname\": \"他们都叫我柯基\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": \"中国\",\n" +
                "      \"countryPinyin\": \"CN\",\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": \"Zhejiang\",\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": \"Ningbo\",\n" +
                "      \"sex\": \"0\",\n" +
                "      \"sexLabel\": \"未知\",\n" +
                "      \"type\": \"5\",\n" +
                "      \"typeLabel\": \"公众号\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"fmessage\",\n" +
                "      \"weChatNo\": \"\",\n" +
                "      \"weChatNickname\": \"朋友推荐消息\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": null,\n" +
                "      \"countryPinyin\": null,\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": null,\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": null,\n" +
                "      \"sex\": \"0\",\n" +
                "      \"sexLabel\": \"未知\",\n" +
                "      \"type\": \"4\",\n" +
                "      \"typeLabel\": \"官方杂号\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"gh_ac8ce63d2944\",\n" +
                "      \"weChatNo\": \"wenjian5555\",\n" +
                "      \"weChatNickname\": \"班级小管家服务\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": \"中国\",\n" +
                "      \"countryPinyin\": \"CN\",\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": \"Beijing\",\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": \"Haidian\",\n" +
                "      \"sex\": \"0\",\n" +
                "      \"sexLabel\": \"未知\",\n" +
                "      \"type\": \"5\",\n" +
                "      \"typeLabel\": \"公众号\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"wxid_barfs7fw8w5922\",\n" +
                "      \"weChatNo\": \"srdzxtc\",\n" +
                "      \"weChatNickname\": \"A华建系统门窗(窗大狮门窗)\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": \"中国\",\n" +
                "      \"countryPinyin\": \"CN\",\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": \"Henan\",\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": \"Zhengzhou\",\n" +
                "      \"sex\": \"1\",\n" +
                "      \"sexLabel\": \"男\",\n" +
                "      \"type\": \"1\",\n" +
                "      \"typeLabel\": \"个微\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"39207418928@chatroom\",\n" +
                "      \"weChatNo\": \"\",\n" +
                "      \"weChatNickname\": \"安太堡战友群\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": null,\n" +
                "      \"countryPinyin\": null,\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": null,\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": null,\n" +
                "      \"sex\": \"0\",\n" +
                "      \"sexLabel\": \"未知\",\n" +
                "      \"type\": \"3\",\n" +
                "      \"typeLabel\": \"群组\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"wxid_2wffx1vz6t0w22\",\n" +
                "      \"weChatNo\": \"ywy2804279011\",\n" +
                "      \"weChatNickname\": \"小小爱\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": \"中国\",\n" +
                "      \"countryPinyin\": \"CN\",\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": \"Shanxi\",\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": \"Shuozhou\",\n" +
                "      \"sex\": \"1\",\n" +
                "      \"sexLabel\": \"男\",\n" +
                "      \"type\": \"1\",\n" +
                "      \"typeLabel\": \"个微\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"45411596543@chatroom\",\n" +
                "      \"weChatNo\": \"\",\n" +
                "      \"weChatNickname\": \"2024CIC小家withoutfaculty\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": null,\n" +
                "      \"countryPinyin\": null,\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": null,\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": null,\n" +
                "      \"sex\": \"0\",\n" +
                "      \"sexLabel\": \"未知\",\n" +
                "      \"type\": \"3\",\n" +
                "      \"typeLabel\": \"群组\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"gh_5fdec78f8ad5\",\n" +
                "      \"weChatNo\": \"code-nav-helper\",\n" +
                "      \"weChatNickname\": \"编程导航小助手\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": \"中国\",\n" +
                "      \"countryPinyin\": \"CN\",\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": \"Shanghai\",\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": \"Xuhui\",\n" +
                "      \"sex\": \"0\",\n" +
                "      \"sexLabel\": \"未知\",\n" +
                "      \"type\": \"5\",\n" +
                "      \"typeLabel\": \"公众号\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"qmessage\",\n" +
                "      \"weChatNo\": \"\",\n" +
                "      \"weChatNickname\": \"QQ离线消息\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": null,\n" +
                "      \"countryPinyin\": null,\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": null,\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": null,\n" +
                "      \"sex\": \"0\",\n" +
                "      \"sexLabel\": \"未知\",\n" +
                "      \"type\": \"1\",\n" +
                "      \"typeLabel\": \"个微\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"gh_238dd60b9679\",\n" +
                "      \"weChatNo\": \"\",\n" +
                "      \"weChatNickname\": \"微软文档\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": \"中国\",\n" +
                "      \"countryPinyin\": \"CN\",\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": \"Beijing\",\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": \"Haidian\",\n" +
                "      \"sex\": \"0\",\n" +
                "      \"sexLabel\": \"未知\",\n" +
                "      \"type\": \"5\",\n" +
                "      \"typeLabel\": \"公众号\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"gh_1befdb502edf\",\n" +
                "      \"weChatNo\": \"\",\n" +
                "      \"weChatNickname\": \"沸腾笔记\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": \"中国\",\n" +
                "      \"countryPinyin\": \"CN\",\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": \"Hubei\",\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": \"Yangyang\",\n" +
                "      \"sex\": \"0\",\n" +
                "      \"sexLabel\": \"未知\",\n" +
                "      \"type\": \"5\",\n" +
                "      \"typeLabel\": \"公众号\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"49625093162@chatroom\",\n" +
                "      \"weChatNo\": \"\",\n" +
                "      \"weChatNickname\": \"七号楼御街 牛街校园餐\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": null,\n" +
                "      \"countryPinyin\": null,\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": null,\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": null,\n" +
                "      \"sex\": \"0\",\n" +
                "      \"sexLabel\": \"未知\",\n" +
                "      \"type\": \"3\",\n" +
                "      \"typeLabel\": \"群组\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"gh_237822f534f4\",\n" +
                "      \"weChatNo\": \"\",\n" +
                "      \"weChatNickname\": \"兴业银行招聘\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": \"中国\",\n" +
                "      \"countryPinyin\": \"CN\",\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": \"Fujian\",\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": \"Fuzhou\",\n" +
                "      \"sex\": \"0\",\n" +
                "      \"sexLabel\": \"未知\",\n" +
                "      \"type\": \"5\",\n" +
                "      \"typeLabel\": \"公众号\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"medianote\",\n" +
                "      \"weChatNo\": \"\",\n" +
                "      \"weChatNickname\": \"语音记事本\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": null,\n" +
                "      \"countryPinyin\": null,\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": null,\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": null,\n" +
                "      \"sex\": \"0\",\n" +
                "      \"sexLabel\": \"未知\",\n" +
                "      \"type\": \"4\",\n" +
                "      \"typeLabel\": \"官方杂号\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"wxid_tdacifeijush22\",\n" +
                "      \"weChatNo\": \"xiongmaomei666\",\n" +
                "      \"weChatNickname\": \"售后客服\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": \"中国\",\n" +
                "      \"countryPinyin\": \"CN\",\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": null,\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": null,\n" +
                "      \"sex\": \"1\",\n" +
                "      \"sexLabel\": \"男\",\n" +
                "      \"type\": \"1\",\n" +
                "      \"typeLabel\": \"个微\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"wxid_zd2ptafnups422\",\n" +
                "      \"weChatNo\": \"dong15721667402\",\n" +
                "      \"weChatNickname\": \"白云\",\n" +
                "      \"weChatRemark\": \"妈妈\",\n" +
                "      \"country\": null,\n" +
                "      \"countryPinyin\": null,\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": null,\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": null,\n" +
                "      \"sex\": \"0\",\n" +
                "      \"sexLabel\": \"未知\",\n" +
                "      \"type\": \"1\",\n" +
                "      \"typeLabel\": \"个微\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"wxid_clri1a9c7y4k22\",\n" +
                "      \"weChatNo\": \"zjy2466478779\",\n" +
                "      \"weChatNickname\": \"独树一帜\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": null,\n" +
                "      \"countryPinyin\": \"HK\",\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": \"Wong Tai Sin\",\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": null,\n" +
                "      \"sex\": \"2\",\n" +
                "      \"sexLabel\": \"女\",\n" +
                "      \"type\": \"1\",\n" +
                "      \"typeLabel\": \"个微\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"gh_47c4eae89929\",\n" +
                "      \"weChatNo\": \"guanglingservice\",\n" +
                "      \"weChatNickname\": \"广聆意见箱\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": \"中国\",\n" +
                "      \"countryPinyin\": \"CN\",\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": \"Anhui\",\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": \"Hefei\",\n" +
                "      \"sex\": \"0\",\n" +
                "      \"sexLabel\": \"未知\",\n" +
                "      \"type\": \"5\",\n" +
                "      \"typeLabel\": \"公众号\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"wxid_z506i8owor3e22\",\n" +
                "      \"weChatNo\": \"\",\n" +
                "      \"weChatNickname\": \"宽容\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": null,\n" +
                "      \"countryPinyin\": null,\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": null,\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": null,\n" +
                "      \"sex\": \"0\",\n" +
                "      \"sexLabel\": \"未知\",\n" +
                "      \"type\": \"1\",\n" +
                "      \"typeLabel\": \"个微\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"gh_1f598f5c7d20\",\n" +
                "      \"weChatNo\": \"yo_homo\",\n" +
                "      \"weChatNickname\": \"Marsbox后花园\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": \"中国\",\n" +
                "      \"countryPinyin\": \"CN\",\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": \"Shanghai\",\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": \"Huangpu\",\n" +
                "      \"sex\": \"0\",\n" +
                "      \"sexLabel\": \"未知\",\n" +
                "      \"type\": \"5\",\n" +
                "      \"typeLabel\": \"公众号\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"22860117246@chatroom\",\n" +
                "      \"weChatNo\": \"\",\n" +
                "      \"weChatNickname\": \"01班小可爱设计试学群\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": null,\n" +
                "      \"countryPinyin\": null,\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": null,\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": null,\n" +
                "      \"sex\": \"0\",\n" +
                "      \"sexLabel\": \"未知\",\n" +
                "      \"type\": \"3\",\n" +
                "      \"typeLabel\": \"群组\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"wxid_anqrclguxz3122\",\n" +
                "      \"weChatNo\": \"a80380066\",\n" +
                "      \"weChatNickname\": \"平安是福\",\n" +
                "      \"weChatRemark\": \"王鑫\",\n" +
                "      \"country\": \"中国\",\n" +
                "      \"countryPinyin\": \"CN\",\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": \"Shanxi\",\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": \"Jincheng\",\n" +
                "      \"sex\": \"1\",\n" +
                "      \"sexLabel\": \"男\",\n" +
                "      \"type\": \"1\",\n" +
                "      \"typeLabel\": \"个微\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"wxid_i15pe5jl6v0j22\",\n" +
                "      \"weChatNo\": \"lrlrlrlr886\",\n" +
                "      \"weChatNickname\": \"九分满\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": \"中国\",\n" +
                "      \"countryPinyin\": \"CN\",\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": null,\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": null,\n" +
                "      \"sex\": \"1\",\n" +
                "      \"sexLabel\": \"男\",\n" +
                "      \"type\": \"1\",\n" +
                "      \"typeLabel\": \"个微\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"gh_8cf65cca1b22\",\n" +
                "      \"weChatNo\": \"\",\n" +
                "      \"weChatNickname\": \"陵川客运中心\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": \"中国\",\n" +
                "      \"countryPinyin\": \"CN\",\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": \"Shanxi\",\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": \"Jincheng\",\n" +
                "      \"sex\": \"0\",\n" +
                "      \"sexLabel\": \"未知\",\n" +
                "      \"type\": \"5\",\n" +
                "      \"typeLabel\": \"公众号\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"43800202545@chatroom\",\n" +
                "      \"weChatNo\": \"\",\n" +
                "      \"weChatNickname\": \"牛马开发小组\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": null,\n" +
                "      \"countryPinyin\": null,\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": null,\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": null,\n" +
                "      \"sex\": \"0\",\n" +
                "      \"sexLabel\": \"未知\",\n" +
                "      \"type\": \"3\",\n" +
                "      \"typeLabel\": \"群组\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"floatbottle\",\n" +
                "      \"weChatNo\": \"\",\n" +
                "      \"weChatNickname\": \"漂流瓶\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": null,\n" +
                "      \"countryPinyin\": null,\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": null,\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": null,\n" +
                "      \"sex\": \"0\",\n" +
                "      \"sexLabel\": \"未知\",\n" +
                "      \"type\": \"4\",\n" +
                "      \"typeLabel\": \"官方杂号\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"wxid_zww3gc88bino22\",\n" +
                "      \"weChatNo\": \"ws18235388655\",\n" +
                "      \"weChatNickname\": \"凨＾不止\",\n" +
                "      \"weChatRemark\": \"文鸿苑621\",\n" +
                "      \"country\": \"中国\",\n" +
                "      \"countryPinyin\": \"CN\",\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": \"Shanxi\",\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": \"Yangquan\",\n" +
                "      \"sex\": \"1\",\n" +
                "      \"sexLabel\": \"男\",\n" +
                "      \"type\": \"1\",\n" +
                "      \"typeLabel\": \"个微\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"wxid_ye8twcr2bo0g22\",\n" +
                "      \"weChatNo\": \"\",\n" +
                "      \"weChatNickname\": \"张扬\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": null,\n" +
                "      \"countryPinyin\": null,\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": null,\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": null,\n" +
                "      \"sex\": \"0\",\n" +
                "      \"sexLabel\": \"未知\",\n" +
                "      \"type\": \"1\",\n" +
                "      \"typeLabel\": \"个微\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"weChatUid\": \"gh_43f2581f6fd6\",\n" +
                "      \"weChatNo\": \"WeRun-WeChat\",\n" +
                "      \"weChatNickname\": \"微信运动\",\n" +
                "      \"weChatRemark\": \"\",\n" +
                "      \"country\": \"中国\",\n" +
                "      \"countryPinyin\": \"CN\",\n" +
                "      \"province\": null,\n" +
                "      \"provincePinyin\": \"Guangdong\",\n" +
                "      \"city\": null,\n" +
                "      \"cityPinyin\": \"Guangzhou\",\n" +
                "      \"sex\": \"0\",\n" +
                "      \"sexLabel\": \"未知\",\n" +
                "      \"type\": \"5\",\n" +
                "      \"typeLabel\": \"公众号\"\n" +
                "    }]";
//        return JSON.parseObject(json, List.class);
        List<WxPpWcfContactsResp> wxPpWcfContactsResps = weChatDllService.queryContactsList();
        return wxPpWcfContactsResps;
    }
}
