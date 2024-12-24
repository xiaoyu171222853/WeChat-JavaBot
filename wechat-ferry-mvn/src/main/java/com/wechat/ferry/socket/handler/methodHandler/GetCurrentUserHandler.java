package com.wechat.ferry.socket.handler.methodHandler;

import com.wechat.ferry.service.WeChatDllService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

// 获取当前登录用户信息
@Component
public class GetCurrentUserHandler implements MethodHandler {
    @Resource
    private WeChatDllService websChatDllService;

    @Override
    public String getMethodName() {
        return "getCurrentUser";
    }

    @Override
    public Object handle(Map<String, Object> params) {
        // 返回用户信息queryLoginWeChatInfo
        return websChatDllService.queryLoginWeChatInfo();
    }
}