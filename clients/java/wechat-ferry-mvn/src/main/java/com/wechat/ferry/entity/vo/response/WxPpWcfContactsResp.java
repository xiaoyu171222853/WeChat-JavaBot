package com.wechat.ferry.entity.vo.response;


import lombok.Data;

/**
 * 请求出参-个微WCF联系人
 *
 * @author chandler
 * @date 2024/10/02 17:01
 */
@Data
public class WxPpWcfContactsResp {

    /**
     * 微信内部识别号UID
     * 原始微信账号ID，以"wxid_"开头，初始默认的微信ID=微信号。
     */
    private String weChatUid;

    /**
     * 微信号
     */
    private String weChatNo;

    /**
     * 微信昵称
     */
    private String weChatNickname;

    /**
     * 联系人备注
     */
    private String weChatRemark;

    /**
     * 国家
     */
    private String country;

    /**
     * 国家拼音
     */
    private String countryPinyin;

    /**
     * 省/州
     */
    private String province;

    /**
     * 省/州拼音
     */
    private String provincePinyin;

    /**
     * 城市
     */
    private String city;

    /**
     * 城市拼音
     */
    private String cityPinyin;

    /**
     * 性别
     */
    private String sex;

    /**
     * 性别-翻译
     */
    private String sexLabel;

    /**
     * 类型
     */
    private String type;

    /**
     * 类型-翻译
     */
    private String typeLabel;

}
