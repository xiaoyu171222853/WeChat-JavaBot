package com.wechat.ferry.socket.handler.methodHandler;

import java.util.Map;

public interface MethodHandler {
    String getMethodName();
    /**
     * 处理服务端请求的方法。
     * @param params 服务端传递的参数
     * @return 返回结果
     */
    Object handle(Map<String, Object> params);
}
