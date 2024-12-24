package com.wechat.ferry.swing;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class MainInterface {

    public void showMainInterface() throws UnsupportedEncodingException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException, FileNotFoundException {
        // 主框架
        JFrame mainFrame = new JFrame("主界面");
        mainFrame.setSize(900, 600);

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);  // 窗口居中显示
        mainFrame.setResizable(false);

        // 设置应用程序的全局字体为支持中文的字体
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        Font font = new Font("Microsoft YaHei", Font.PLAIN, 16);
        UIManager.put("Label.font", font);
        UIManager.put("Button.font", font);
        UIManager.put("TextArea.font", font);
        UIManager.put("TextField.font", font);

        // 使用 CardLayout 来管理多个模块
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        // 创建不同的模块面板
        JPanel homePanel = new HomePanel().createHomePanel();
        JPanel logPanel = new LogPanel().createLogPanel();
        JPanel pluginPanel = new PluginPanel().createPluginPanel();

        // 将模块添加到 CardLayout 面板
        mainPanel.add(homePanel, "首页");
        mainPanel.add(logPanel, "日志");
        mainPanel.add(pluginPanel, "插件列表");

        // 创建侧边栏
        JPanel sidePanel = new SidePanel(cardLayout, mainPanel).createSidePanel();

        // 设置整体布局
        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(sidePanel, BorderLayout.WEST);  // 侧边栏
        mainFrame.add(mainPanel, BorderLayout.CENTER);  // 主内容区

        // 显示主界面
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new MainInterface().showMainInterface();
            } catch (UnsupportedEncodingException | UnsupportedLookAndFeelException | ClassNotFoundException |
                     InstantiationException | IllegalAccessException | FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
