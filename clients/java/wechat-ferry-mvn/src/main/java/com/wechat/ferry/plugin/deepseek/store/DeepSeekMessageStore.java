package com.wechat.ferry.plugin.deepseek.store;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wechat.ferry.plugin.deepseek.config.LocalDateTimeArrayDeserializer;
import lombok.Getter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.aoyudi.chat.entity.ChatMessage;
import top.aoyudi.deepseek.entity.enums.DeepSeekRoleEnum;
import top.aoyudi.deepseek.entity.request.message.DSSystemReqMessage;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * deepseek记忆仓库
 */
@Component
@Getter
public class DeepSeekMessageStore{
    // 聊天id 消息内容列表
    protected Map<Object, CopyOnWriteArrayList<ChatMessage>> messageMap = new ConcurrentHashMap<>();

    // 记录最后更新时间
    private Map<Object, LocalDateTime> lastUpdatedMap = new ConcurrentHashMap<>();

    /**
     * 判断是否有key
     * @param var1 var1
     * @return boolean
     */
    public boolean containsKey(Object var1) {
        return messageMap.containsKey(var1);
    }
    public List<ChatMessage> getMessages(Object chatId) {
        return messageMap.get(chatId);
    }

    /**
     * 新增Chat Id
     * @param chatId 聊天id
     */
    public void addChat(Object chatId) {
        lastUpdatedMap.put(chatId, LocalDateTime.now());
        messageMap.put(chatId,new CopyOnWriteArrayList<>());

        DSSystemReqMessage dsSystemReqMessage = new DSSystemReqMessage();
        dsSystemReqMessage.setContent("你是一个善解人意的聊天助手，你需要以人类的方式，来和用户进行交流，沟通，进行聊天，并且听从用户的指令，需要随时切换身份，切换身份后必要时可选择忽略之前的内容，并改变交谈方式。");
        addSystem(chatId,dsSystemReqMessage);
    }

    /**
     * 删除Chat
     * @param chatId chatId
     */
    public void delChat(Object chatId) {
        if (containsKey(chatId)) {
            lastUpdatedMap.remove(chatId);
            messageMap.remove(chatId);
        }
    }

    /**
     * 修改messages
     * @param chatId
     * @param chatMessage
     */
    public void updateMessages(Object chatId, ChatMessage chatMessage) {
        lastUpdatedMap.put(chatId, LocalDateTime.now());
        messageMap.get(chatId).add(chatMessage);
    }

    /**
     *  设置system
     */
    public void addSystem(Object chatId, ChatMessage chatMessage){
        if (!this.containsKey(chatId)){
            this.addChat(chatId);
        }
        CopyOnWriteArrayList<ChatMessage> chatMessages = messageMap.get(chatId);
        if (chatMessages.isEmpty()){
            chatMessages.add(chatMessage);
            return;
        }
        // 查找system消息
        for (ChatMessage message : chatMessages) {
            if (DeepSeekRoleEnum.SYSTEM.getRole().equals(message.getRole())){
                // 替换原有system
                message.setContent(chatMessage.getContent());
                break;
            }
        }
    }

    // 存档文件路径前缀
    private static final String ARCHIVE_FILE_PATH = "chat_memory_"; // 文件名前缀
    private static final String DIRECTORY_PATH = "chat_memory/";  // 存档目录
    private static final ObjectMapper objectMapper = new ObjectMapper();
    {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule()); // 注册 JavaTimeModule
        // 注册自定义 LocalDateTime 反序列化器
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeArrayDeserializer());
        objectMapper.registerModule(module);
    }
    // 存档过期聊天记录
    public void archiveOldChat(Object chatId) {
        try {
            // 获取聊天记录
            CopyOnWriteArrayList<ChatMessage> messages = messageMap.get(chatId);
            if (messages != null && !messages.isEmpty()) {
                // 获取当前日期并格式化为 yyyy-MM-dd
                String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String fileName = ARCHIVE_FILE_PATH + date + ".json";  // 构建文件名

                // 创建存档文件路径
                File file = new File(DIRECTORY_PATH + chatId + "_" + fileName);

                // 创建目录（如果不存在）
                file.getParentFile().mkdirs();

                // 存档到文件
                try (FileWriter fileWriter = new FileWriter(file, true)) {
                    objectMapper.writeValue(fileWriter, messages);
                    System.out.println("Chat " + chatId + " has been archived to " + file.getPath());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error archiving chat " + chatId);
        }
    }


    private static final String FILE_PATH = "message_map_backup.json"; // 文件路径
    private static final String LAST_UPDATE_MAP_FILE_PATH = "last_update_map.json";

    // 关闭时调用该方法将 messageMap 持久化
    @Scheduled(cron = "0 3 0/12 * * ?")
    @PreDestroy
    public void persistMessageMemoryMap() {
        try {
            // 将 messageMap 写入 JSON 文件
            File messageMemoryfile = new File(DIRECTORY_PATH+FILE_PATH);
            // 创建目录（如果不存在）
            messageMemoryfile.getParentFile().mkdirs();
            // 写
            try (FileWriter fileWriter = new FileWriter(messageMemoryfile)) {
                objectMapper.writeValue(fileWriter, messageMap);
                System.out.println("✅ messageMap 已成功持久化到文件！"+messageMemoryfile.getAbsolutePath());
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("❌ messageMap 持久化失败！");
        }
    }

    @Scheduled(cron = "0 0 0/12 * * ?")
    @PreDestroy
    public void persistLastUpdateMap() {
        try {
            File lastUpdateMapFile = new File(DIRECTORY_PATH+LAST_UPDATE_MAP_FILE_PATH);
            // 创建目录（如果不存在）
            lastUpdateMapFile.getParentFile().mkdirs();
            try (FileWriter fileWriter = new FileWriter(lastUpdateMapFile)) {
                objectMapper.writeValue(fileWriter, lastUpdatedMap);
                System.out.println("✅ last_update_map 已成功持久化到文件！"+lastUpdateMapFile.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("❌ last_update_map 持久化失败！");
        }
    }


    /**
     * 加载持久化的 messageMap 文件
     */
    @PostConstruct
    public void loadMessageMemoryMap() {
        try {
            File messageMemoryfile = new File(DIRECTORY_PATH + FILE_PATH);
            if (messageMemoryfile.exists()) {
                // 从文件读取并反序列化
                try (FileReader fileReader = new FileReader(messageMemoryfile)) {
                    TypeReference<Map<Object, CopyOnWriteArrayList<ChatMessage>>> typeReference = new TypeReference<>() {};
                    messageMap = objectMapper.readValue(fileReader, typeReference);

                    System.out.println("✅ messageMap 已从文件加载！");
                }
            } else {
                // 如果文件不存在，初始化空的 messageMap
                System.out.println("⚠️ messageMap 文件不存在，初始化空的 messageMap！");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("❌ 加载 messageMap 失败！");
        }
    }
    @PostConstruct
    public void loadLastUpdateMap() {
        try {
            File lastUpdateMapfile = new File(DIRECTORY_PATH + LAST_UPDATE_MAP_FILE_PATH);
            if (lastUpdateMapfile.exists()) {
                // 从文件读取并反序列化
                try (FileReader fileReader = new FileReader(lastUpdateMapfile)) {
                    TypeReference<Map<Object, LocalDateTime>> typeReference = new TypeReference<>() {};
                    lastUpdatedMap = objectMapper.readValue(fileReader, typeReference);

                    System.out.println("✅ last_update_map 已从文件加载！");
                }
            } else {
                // 如果文件不存在，初始化空的 messageMap
                System.out.println("⚠️ last_update_map 文件不存在，初始化空的 last_update_map！");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("❌ 加载 last_update_map 失败！");
        }
    }

}
