package com.wechat.ferry.handle;

import com.wechat.ferry.handle.methodHandler.GetCurrentUserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Component
public class HandlerRegistry {
    private static final Map<String, MethodHandler> handlers = new HashMap<>();
    // 通过构造器注入需要的 Handler
    @Autowired
    public HandlerRegistry(GetCurrentUserHandler getCurrentUserHandler) {
        // 注册具体的方法处理器
        register("getCurrentUser", getCurrentUserHandler);
    }

    public static void register(String method, MethodHandler handler) {
        handlers.put(method, handler);
    }

    public MethodHandler getHandler(String method) {
        return handlers.get(method);
    }
}
