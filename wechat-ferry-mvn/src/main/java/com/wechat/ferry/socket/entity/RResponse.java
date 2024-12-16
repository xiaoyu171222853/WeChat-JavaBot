package com.wechat.ferry.socket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RResponse {

    private String code;    // 状态码

    private Object result;  // 返回信息

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
