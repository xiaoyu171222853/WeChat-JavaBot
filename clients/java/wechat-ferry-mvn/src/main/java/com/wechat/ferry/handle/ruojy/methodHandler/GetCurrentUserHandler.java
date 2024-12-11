package com.wechat.ferry.handle.ruojy.methodHandler;

import com.wechat.ferry.handle.ruojy.MethodHandler;
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
    public Object handle(Map<String, Object> params) {
        // 返回用户信息queryLoginWeChatInfo
        return websChatDllService.queryLoginWeChatInfo();
    }
}
