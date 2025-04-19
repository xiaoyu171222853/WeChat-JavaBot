package com.wechat.ferry.entity.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 请求入参-个微WCF发送拍一拍消息
 *
 * @author chandler
 * @date 2024-10-06 15:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "wxPpWcfPatOnePatMsgReq", description = "个微WCF发送拍一拍消息请求入参")
public class WxPpWcfPatOnePatMsgReq extends WxPpWcfRequest {

    /**
     * 消息接收人
     * 消息接收人，私聊为 wxid（wxid_xxxxxxxxxxxxxx）
     * 群聊为 roomid（xxxxxxxxxx@chatroom）
     */
    @NotBlank(message = "消息接收人不能为空")
    @ApiModelProperty(value = "消息接收人")
    private String recipient;

    /**
     * 要拍的人的wxid
     */
    @NotBlank(message = "要拍的人的wxid不能为空")
    @ApiModelProperty(value = "要拍的人的wxid")
    private String patUser;

}
