package com.wechat.ferry.plugin.deepseek.config;

import com.wechat.ferry.entity.vo.MessageRequest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import top.aoyudi.deepseek.entity.model.DeepSeekChatModel;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@EnableAsync(proxyTargetClass = true)
@EnableCaching(proxyTargetClass = true)
@Configuration
public class DeepSeekModelConfig {

    /**
     * 无思考 非流式 聊天
     * @return DeepSeekChatModel
     */
    @Bean
    public DeepSeekChatModel deepSeekChatModel() {
        return DeepSeekChatModel.builder()
                .stream(false)
                .build();
    }

    // 定义一个线程安全的消息队列，并将其暴露为Bean
    @Bean
    public Queue<MessageRequest> messageQueue() {
        return new ConcurrentLinkedQueue<>();
    }
}
