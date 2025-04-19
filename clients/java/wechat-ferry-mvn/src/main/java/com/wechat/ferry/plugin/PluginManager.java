package com.wechat.ferry.plugin;

import com.wechat.ferry.entity.dto.WxPpMsgDTO;
import com.wechat.ferry.entity.vo.MessageRequest;
import com.wechat.ferry.entity.vo.request.*;
import com.wechat.ferry.service.WeChatDllService;
import com.wechat.ferry.swing.SettingsWindow;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

import static com.wechat.ferry.enums.MessageTypeEnum.*;

@Log4j2
@Service
public class PluginManager {

    @Resource
    private Queue<MessageRequest> messageQueue;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private volatile boolean isProcessing = true;

    @Resource
    private WeChatDllService weChatDllService;

    @Resource
    List<ChatBotPlugin> devPlugins = new ArrayList<>();

    public static List<ChatBotPlugin> startedPlugins = new ArrayList<>();
    public static List<ChatBotPlugin> destroyedPlugins = new ArrayList<>();


    public static void destroyPlugin(ChatBotPlugin plugin) {
        plugin.destroy();
        startedPlugins.remove(plugin);
        destroyedPlugins.add(plugin);
    }

    public static void startPlugin(ChatBotPlugin plugin) {
        plugin.initialize();
        startedPlugins.add(plugin);
        destroyedPlugins.remove(plugin);
    }

    /**
     * 初始化内置插件和消息处理线程
     */
    @PostConstruct
    private void init(){
        startedPlugins=devPlugins;
        startedPlugins.forEach(ChatBotPlugin::initialize);
        startMessageProcessor();
    }

    /**
     * 启动消息处理线程
     */
    private void startMessageProcessor() {
        executorService.submit(() -> {
            while (isProcessing) {
                try {
                    processQueue();
                    Thread.sleep(10000); // 每100ms检查一次队列
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    log.error("处理消息队列时出错", e);
                }
            }
        });
    }

    /**
     * 处理群聊消息
     */
    public void handleGroupMessage(WxPpMsgDTO wxPpMsgDTO) {
        startedPlugins.forEach(plugin -> {
            plugin.handleGroupMessage(wxPpMsgDTO);
        });
    }

    /**
     * 处理群聊艾特我消息
     */
    public void handleGroupAtMeMessage(WxPpMsgDTO wxPpMsgDTO) {
        startedPlugins.forEach(plugin -> {
            plugin.handleGroupAtMeMessage(wxPpMsgDTO);
        });
    }

    /**
     * 处理个人消息
     */
    public void handlePersonalMessage(WxPpMsgDTO wxPpMsgDTO) {
        startedPlugins.forEach(plugin -> {
            plugin.handlePersonalMessage(wxPpMsgDTO);
        });
    }

    /**
     * 处理订阅号消息
     * @param wxPpMsgDTO 消息内容
     * @return 插件处理后的响应
     */
//    public void handleSubscriptionMessage(WxPpMsgDTO wxPpMsgDTO) {
//        for (ChatBotPlugin plugin : sortedPlugins) {
//            MessageRequest request = plugin.handleSubscriptionMessage(wxPpMsgDTO);
//            if (request != null) {
//                // 将返回的消息请求添加到队列
//                messageQueue.add(request);
//                startQueueProcessing(30);
//                break;
//            }
//        }
//    }

    /**
     * 框架内置功能
     */
    public void frameWorkFunction(WxPpMsgDTO dto){
        switch (dto.getContent()){
            case "菜单":
                String menuContent = SettingsWindow.getMenuContent();
                WxPpWcfSendTextMsgReq textMsgReq = new WxPpWcfSendTextMsgReq();
                textMsgReq.setMsgText(menuContent);
                textMsgReq.setRecipient(dto.getRoomId());
                List<String> user = new ArrayList<>();
                user.add(dto.getSender());
                textMsgReq.setAtUsers(user);

                messageQueue.offer(MessageRequest.create(TEXT, textMsgReq));
                break;
            default:
                break;
        }
    }

    // 消息队列处理器
    private void processQueue() {
        MessageRequest request = messageQueue.poll();
        if (request != null) {
            try {
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
            } catch (Exception e) {
                log.error("处理消息失败: {}", request, e);
            }
        }
    }

    @PreDestroy
    private void preDestroy() {
        isProcessing = false;
        if (executorService != null) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        devPlugins.forEach(ChatBotPlugin::destroy);
    }
}
