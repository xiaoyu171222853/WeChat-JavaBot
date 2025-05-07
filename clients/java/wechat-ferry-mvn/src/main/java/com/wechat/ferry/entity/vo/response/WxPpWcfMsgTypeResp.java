package com.wechat.ferry.entity.vo.response;


import lombok.Data;

/**
 * 请求出参-个微WCF消息类型
 *
 * @author chandler
 * @date 2024/10/01 21:26
 */
@Data
public class WxPpWcfMsgTypeResp {

    /**
     * 类型编号
     */
    private Integer id;

    /**
     * 类型名称
     */
    private String name;

}
