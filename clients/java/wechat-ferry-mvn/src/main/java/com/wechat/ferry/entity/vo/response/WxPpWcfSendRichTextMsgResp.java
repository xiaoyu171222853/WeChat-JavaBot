package com.wechat.ferry.entity.vo.response;


import lombok.Data;

/**
 * 请求出参-个微WCF发送富文本消息
 *
 * @author chandler
 * @date 2024/10/06 15:46
 */
@Data
public class WxPpWcfSendRichTextMsgResp {

    /**
     * 类型编号
     */
    private Integer id;

    /**
     * 类型名称
     */
    private String name;

}
