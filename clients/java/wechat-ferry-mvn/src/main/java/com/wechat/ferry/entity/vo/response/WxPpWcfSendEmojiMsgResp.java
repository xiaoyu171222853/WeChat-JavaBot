package com.wechat.ferry.entity.vo.response;


import lombok.Data;

/**
 * 请求出参-个微WCF发送GIF消息
 *
 * @author chandler
 * @date 2024/10/04 23:13
 */
@Data
public class WxPpWcfSendEmojiMsgResp {

    /**
     * 类型编号
     */
    private Integer id;

    /**
     * 类型名称
     */
    private String name;

}
