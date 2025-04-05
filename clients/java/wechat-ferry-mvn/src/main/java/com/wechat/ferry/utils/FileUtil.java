package com.wechat.ferry.utils;

import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import java.io.*;

/**
 * 文件操作工具
 */
@Log4j2
public class FileUtil {
    public static String readFile(String filePath) {
        File menuFile = new File(filePath);
        if (menuFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(menuFile))) {
                String line;
                StringBuilder content = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                return content.toString();
            } catch (IOException e) {
                log.error("读取文件失败", e);
                return "文件读取错误，请检查权限。";
            }
        } else {
            log.warn("menu 文件不存在");
            return null;
        }
    }

    /**
     * 创建文件
     * @param filePath
     */
    public static void createFile(String filePath){
        File menuFile = new File(filePath);
        try {
            if (menuFile.createNewFile()) {
                log.info("文件创建成功");
            } else {
                log.error("文件创建失败");
            }
        } catch (IOException e) {
            log.error("创建文件失败", e);
        }
    }

    /**
     * 写文件
     * @param filePath
     * @param contentToWrite
     * @return
     */
    public static void writeFile(String filePath, String contentToWrite) {
        File menuFile = new File(filePath);
        try {
            if (menuFile.exists()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(menuFile))) {
                    writer.write(contentToWrite);
                }
            } else {
                log.error(filePath+"文件不存在");
            }
        } catch (IOException e) {
            log.error(filePath+"写入文件失败", e);
        }
    }
}
