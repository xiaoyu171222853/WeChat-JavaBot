package com.wechat.ruojy.handler.methodHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

@Component
public class HandlerRegistry {

    private static final Map<String, MethodHandler> handlers = new HashMap<>();

    @Autowired
    private List<MethodHandler> methodHandlers;  // 自动注入所有MethodHandler的实现

    @PostConstruct
    public void init() {
        // 自动注册所有MethodHandler实例
        for (MethodHandler handler : methodHandlers) {
            register(handler.getMethodName(), handler);  // 假设MethodHandler有getMethodName()方法返回唯一标识符
        }
    }

    public static void register(String method, MethodHandler handler) {
        handlers.put(method, handler);
    }

    public MethodHandler getHandler(String method) {
        return handlers.get(method);
    }
}
