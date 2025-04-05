package com.wechat.ferry.entity.vo.response;


import lombok.Data;

/**
 * 请求出参-个微WCF发送XML消息
 *
 * @author chandler
 * @date 2024/10/04 23:11
 */
@Data
public class WxPpWcfSendXmlMsgResp {

    /**
     * 类型编号
     */
    private Integer id;

    /**
     * 类型名称
     */
    private String name;

}
