package com.wechat.ferry.entity.vo.response;


import lombok.Data;

/**
 * 请求出参-个微WCF发送图像消息
 *
 * @author chandler
 * @date 2024/10/03 10:17
 */
@Data
public class WxPpWcfSendImageMsgResp {

    /**
     * 状态码
     */
    private String code;

    /**
     * 返回信息
     */
    private String msg;

    /**
     * 图片地址
     */
    private String path;

    /**
     * 消息接收人
     * 消息接收人，私聊为 wxid（wxid_xxxxxxxxxxxxxx）
     * 群聊为 roomid（xxxxxxxxxx@chatroom）
     */
    private String recipient;

}
