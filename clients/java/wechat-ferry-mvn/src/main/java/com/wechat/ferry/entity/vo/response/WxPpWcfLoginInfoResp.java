package com.wechat.ferry.entity.vo.response;


import lombok.Data;

/**
 * 请求出参-登录WCF个微信息
 *
 * @author chandler
 * @date 2024/10/05 22:53
 */
@Data
public class WxPpWcfLoginInfoResp {

    /**
     * 微信内部识别号UID
     * 原始微信账号ID，以"wxid_"开头，初始默认的微信ID=微信号。
     */
    private String weChatUid;

    /**
     * 微信昵称
     */
    private String weChatNickname;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 文件/图片等父路径
     */
    private String homePath;

}
