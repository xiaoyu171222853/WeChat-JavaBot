package com.wechat.ferry.plugin;

import org.springframework.stereotype.Service;
import top.ruojy.wxbot.ChatBotPlugin;
import top.ruojy.wxbot.entity.MessageRequest;
import top.ruojy.wxbot.entity.dto.WxPpMsgDTO;
import top.ruojy.wxbot.entity.vo.request.*;
import top.ruojy.wxbot.service.WeChatDllService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

@Service
public class PluginManager {

    // 存储按优先级排序后的插件列表
    private List<ChatBotPlugin> sortedPlugins;

    // 定义一个线程安全的消息队列
    private final Queue<MessageRequest> messageQueue = new ConcurrentLinkedQueue<>();
    private ExecutorService executorService;
    private ScheduledExecutorService scheduler;
    private volatile boolean isProcessing;
    @Resource
    private WeChatDllService weChatDllService;
    /**
     * 插件初始化
     */
    public void initializePlugins(List<ChatBotPlugin> plugins) {
        // 对插件进行排序，确保优先级高的插件排在前面
        sortedPlugins = new ArrayList<>(plugins);
        sortedPlugins.sort((plugin1, plugin2) -> Integer.compare(plugin2.getPriority(), plugin1.getPriority()));

        // 初始化插件
        for (ChatBotPlugin plugin : sortedPlugins) {
            plugin.initialize();
        }
        executorService = Executors.newSingleThreadExecutor();
        scheduler = Executors.newScheduledThreadPool(1);
    }

    /**
     * 插件销毁
     */
    public void destroyPlugins() {
        for (ChatBotPlugin plugin : sortedPlugins) {
            plugin.destroy();
        }
    }

    /**
     * 处理群聊消息
     * @param wxPpMsgDTO 消息内容
     * @return 插件处理后的响应
     */
    public void handleGroupMessage(WxPpMsgDTO wxPpMsgDTO) {
        for (ChatBotPlugin plugin : sortedPlugins) {
            MessageRequest request = plugin.handleGroupMessage(wxPpMsgDTO);
            if (request != null) {
                // 将返回的消息请求添加到队列
                messageQueue.add(request);
                startQueueProcessing(30);
                break;
            }
        }
    }

    /**
     * 处理群聊艾特我消息
     *
     * @return 插件处理后的响应
     */
    public void handleGroupAtMeMessage(WxPpMsgDTO wxPpMsgDTO) {
        for (ChatBotPlugin plugin : sortedPlugins) {
            MessageRequest request = plugin.handleGroupAtMeMessage(wxPpMsgDTO);
            if (request != null) {
                // 将返回的消息请求添加到队列
                messageQueue.add(request);
                startQueueProcessing(30);
                break;
            }
        }
    }

    /**
     * 处理个人消息
     * @param wxPpMsgDTO 消息内容
     * @return 插件处理后的响应
     */
    public void handlePersonalMessage(WxPpMsgDTO wxPpMsgDTO) {
        for (ChatBotPlugin plugin : sortedPlugins) {
            MessageRequest request = plugin.handlePersonalMessage(wxPpMsgDTO);
            if (request != null) {
                // 将返回的消息请求添加到队列
                messageQueue.add(request);
                startQueueProcessing(30);
                break;
            }
        }
    }

    /**
     * 处理订阅号消息
     * @param wxPpMsgDTO 消息内容
     * @return 插件处理后的响应
     */
    public void handleSubscriptionMessage(WxPpMsgDTO wxPpMsgDTO) {
        for (ChatBotPlugin plugin : sortedPlugins) {
            MessageRequest request = plugin.handleSubscriptionMessage(wxPpMsgDTO);
            if (request != null) {
                // 将返回的消息请求添加到队列
                messageQueue.add(request);
                startQueueProcessing(30);
                break;
            }
        }
    }

    // 消息队列处理器（如果有异步处理需求）
    public void processQueue() {
        while (!messageQueue.isEmpty()) {
            MessageRequest request = messageQueue.poll();
            if (request != null) {
                // 根据类型调用相应的发送方法
                switch (request.getMessageTypeEnum()) {
                    case TEXT:
                        weChatDllService.sendTextMsg((WxPpWcfSendTextMsgReq) request.getRequest());
                        break;
                    case EMOJI:
                        weChatDllService.sendEmojiMsg((WxPpWcfSendEmojiMsgReq) request.getRequest());
                        break;
                    case IMAGE:
                        weChatDllService.sendImageMsg((WxPpWcfSendImageMsgReq) request.getRequest());
                        break;
                    case RICHTER:
                        weChatDllService.sendRichTextMsg((WxPpWcfSendRichTextMsgReq) request.getRequest());
                        break;
                    case FILE:
                        weChatDllService.sendFileMsg((WxPpWcfSendFileMsgReq) request.getRequest());
                        break;
                    case XML:
                        weChatDllService.sendXmlMsg((WxPpWcfSendXmlMsgReq) request.getRequest());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    // 启动消息队列处理，并设置超时
    public void startQueueProcessing(long timeoutSeconds) {
        if (!isProcessing) {
            isProcessing = true;

            // 启动处理消息的线程
            executorService.submit(() -> {
                while (isProcessing) {
                    processQueue();
                    try {
                        Thread.sleep(5000); // 每5秒钟检查一次队列
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });

            // 启动超时任务，在指定时间后关闭消息队列处理
            scheduler.schedule(() -> {
                stopQueueProcessing();
            }, timeoutSeconds, TimeUnit.SECONDS);
        }
    }

    // 停止消息队列处理
    public void stopQueueProcessing() {
        if (isProcessing) {
            isProcessing = false;
            System.out.println("消息队列处理已停止.");
        }
    }

}
