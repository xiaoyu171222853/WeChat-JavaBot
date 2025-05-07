package com.wechat.ferry.entity.vo.response;


import lombok.Data;

/**
 * 请求出参-个微WCF发送拍一拍消息
 *
 * @author chandler
 * @date 2024/10/06 15:52
 */
@Data
public class WxPpWcfSendPatOnePatMsgResp {

    /**
     * 类型编号
     */
    private Integer id;

    /**
     * 类型名称
     */
    private String name;

}
