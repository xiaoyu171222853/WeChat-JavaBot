package com.wechat.ferry.handle.ruojy;

import java.util.Map;

public interface MethodHandler {
    /**
     * 处理服务端请求的方法。
     * @param params 服务端传递的参数
     * @return 返回结果
     * @throws Exception 如果发生错误
     */
    Object handle(Map<String, Object> params);
}
