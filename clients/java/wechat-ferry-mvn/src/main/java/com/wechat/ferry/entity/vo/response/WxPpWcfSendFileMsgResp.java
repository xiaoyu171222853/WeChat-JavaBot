package com.wechat.ferry.entity.vo.response;


import lombok.Data;

/**
 * 请求出参-个微WCF发送文件消息
 *
 * @author chandler
 * @date 2024/10/04 23:07
 */
@Data
public class WxPpWcfSendFileMsgResp {

    /**
     * 类型编号
     */
    private Integer id;

    /**
     * 类型名称
     */
    private String name;

}
