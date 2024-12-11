package com.wechat.ferry.entity.ruojy;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RRequest {
    @ApiModelProperty("请求类型 Get/Post")
    private String type;

    @ApiModelProperty("请求方法，客户端中具体方法")
    private String method;

    @ApiModelProperty("请求参数")
    private Object params;
}
