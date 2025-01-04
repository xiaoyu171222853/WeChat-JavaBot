package com.wechat.ferry.swing;

import com.wechat.ferry.utils.FileUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

@Log4j2
@Component
public class SettingsWindow extends JFrame {

    private JTextArea menuTextArea;

    private static final String MENU_CONTENT_TEMPLATE =
        """
        以下为框架自动生成模版：
        🎉 欢迎使用机器人助手！以下是我为您准备的功能：

        1. 聊天与对话
           - 🤖 与我聊天：与机器人进行轻松对话。
           - 📝 角色扮演：选择角色与我一起进行角色扮演游戏。

        2. 娱乐功能
           - 🎲 群内小游戏：参与简单的群内小游戏，与其他成员一较高下！
           - 📜 讲笑话：听我讲笑话，给你带来一丝欢乐。
           - 🎶 随机歌曲推荐：为你推荐好听的歌曲。
           - 📖 成语接龙：和我一起进行成语接龙挑战！

        3. 实用工具
           - ⏰ 天气查询：查询当前天气，了解你所在地区的天气情况。
           - 🗓️ 日期查询：查看当前日期，获取节假日信息。
           - 💡 每日一句：获取每日励志/哲理的一句话。
           - 📊 数据分析：提供简单的数据分析和统计功能。

        5. 帮助与反馈
           - ❓ 帮助文档：查看使用帮助，了解如何使用各项功能。
           - 📬 意见反馈：给我提出你的建议或反馈，我会不断改进。

        操作方式：
        - 插件提供功能
        """;

    @Value("${ruojy.file-path}")
    private static String MENU_FILE;

    public SettingsWindow() {
        setTitle("设置");
        setSize(500, 400);
        setLocationRelativeTo(null);  // 居中显示
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);  // 只关闭当前窗口
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // 文本区域，用于显示和编辑 menu 文件内容
        menuTextArea = new JTextArea();
        menuTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(menuTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 底部按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton saveButton = new JButton("保存");
//        JButton cancelButton = new JButton("取消");

        saveButton.addActionListener(e -> saveMenuFile());

//        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
//        buttonPanel.add(cancelButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        add(panel);

        // 读取文件内容
        loadMenuFile();
    }

    /**
     * 获取菜单内容
     * @return 菜单内容
     */
    public static String getMenuContent(){
        return FileUtil.readFile(MENU_FILE);
    }

    /**
     * 加载菜单
     */
    public void loadMenuFile() {
        String content = FileUtil.readFile(MENU_FILE);
        if (content== null) {
            FileUtil.createFile(MENU_FILE);
            FileUtil.writeFile(MENU_FILE, MENU_CONTENT_TEMPLATE);
            JOptionPane.showMessageDialog(this, "menu 文件不存在，已自动生成并写入模版。", "提示", JOptionPane.INFORMATION_MESSAGE);
            content = FileUtil.readFile(MENU_FILE);
        }
        menuTextArea.setText(content);
    }

    private void saveMenuFile() {
        File menuFile = new File(MENU_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(menuFile))) {
            writer.write(menuTextArea.getText());
            JOptionPane.showMessageDialog(this, "保存成功", "提示", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            log.error("保存 menu 文件失败", e);
            JOptionPane.showMessageDialog(this, "保存失败", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}