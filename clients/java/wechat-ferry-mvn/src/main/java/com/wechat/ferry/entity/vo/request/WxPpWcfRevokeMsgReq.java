package com.wechat.ferry.entity.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 请求入参-撤回消息
 *
 * @author chandler
 * @date 2024-12-25 12:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "wxPpWcfRevokeMsgReq", description = "个微WCF撤回消息请求入参")
public class WxPpWcfRevokeMsgReq extends WxPpWcfRequest {

    /**
     * 消息编号
     */
    @ApiModelProperty(value = "场景")
    private String msgId;

}
