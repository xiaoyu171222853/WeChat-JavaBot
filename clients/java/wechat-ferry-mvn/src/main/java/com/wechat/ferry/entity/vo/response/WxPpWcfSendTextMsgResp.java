package com.wechat.ferry.entity.vo.response;


import lombok.Data;

/**
 * 请求出参-个微WCF发送文本消息
 *
 * @author chandler
 * @date 2024/10/03 10:17
 */
@Data
public class WxPpWcfSendTextMsgResp {

    /**
     * 类型编号
     */
    private Integer id;

    /**
     * 类型名称
     */
    private String name;

}
