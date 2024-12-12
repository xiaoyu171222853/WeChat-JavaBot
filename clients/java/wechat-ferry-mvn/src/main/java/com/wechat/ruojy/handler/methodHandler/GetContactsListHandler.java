package com.wechat.ruojy.handler.methodHandler;

import com.wechat.ferry.service.WeChatDllService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class GetContactsListHandler implements MethodHandler{

    @Resource
    private WeChatDllService weChatDllService;

    @Override
    public String getMethodName() {
        return "getContactsList";
    }

    @Override
    public Object handle(Map<String, Object> params) {
        return weChatDllService.queryContactsList();
    }
}
