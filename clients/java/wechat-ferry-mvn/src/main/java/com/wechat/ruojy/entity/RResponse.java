package com.wechat.ruojy.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RResponse {

    @ApiModelProperty("状态码")
    private String code;

    @ApiModelProperty("返回信息")
    private Object result;

    public static RResponse success(Object data){
        return new RResponse("200",data);
    }
    public static RResponse success(String code,Object data){
        return new RResponse(code,data);
    }
    public static RResponse error(String msg){
        return new RResponse("400",msg);
    }
    public static RResponse error(String code,String msg){
        return new RResponse(code,msg);
    }
}
