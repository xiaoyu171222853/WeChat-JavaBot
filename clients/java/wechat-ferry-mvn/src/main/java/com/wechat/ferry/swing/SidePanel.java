package com.wechat.ferry.swing;

import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Log4j2
public class SidePanel {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public SidePanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
    }

    public JPanel createSidePanel() {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));  // 垂直布局
        sidePanel.setBackground(Color.WHITE);  // 侧边栏背景色设置为白色

        // 设置侧边栏宽度
        sidePanel.setPreferredSize(new Dimension(100, 800));  // 增加宽度为 200px
        sidePanel.setMinimumSize(new Dimension(100, 800));

        // 创建机器人头像
        ImageIcon robotIcon = new ImageIcon(getClass().getResource("/img/otherPhoto.png"));
        if (robotIcon == null) {
            System.out.println("头像图片未找到！");
        }

        // 设置头像为圆形
        JLabel avatarLabel = new JLabel(robotIcon);
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  // 头像居中显示
        avatarLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));  // 设置边距
        avatarLabel.setPreferredSize(new Dimension(100, 100));  // 设置头像大小

        // 设置头像为圆形
        avatarLabel.setIcon(new ImageIcon(robotIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));

        // 创建标签代替按钮
        JLabel homeLabel = createNavLabel("首页", cardLayout, mainPanel, "首页");
        JLabel logLabel = createNavLabel("日志输出", cardLayout, mainPanel, "日志");
        JLabel pluginLabel = createNavLabel("插件列表", cardLayout, mainPanel, "插件列表");

        // 分隔条
//        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
//        separator.setForeground(Color.LIGHT_GRAY);
        // 创建版本信息和设置按钮

        JLabel versionLabel = new JLabel("版本 0.3.0");  // 版本信息
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        versionLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        versionLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel settingsLabel = new JLabel("设置");  // 设置按钮
        settingsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        settingsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        settingsLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));  // 手型光标
        settingsLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            // 在此添加点击设置按钮后的操作
            SwingUtilities.invokeLater(() -> {
                SettingsWindow settingsWindow = new SettingsWindow();
                settingsWindow.setVisible(true);
            });
            }
        });

        // 将组件添加到侧边栏
        sidePanel.add(avatarLabel);
        sidePanel.add(homeLabel);
        sidePanel.add(logLabel);
        sidePanel.add(pluginLabel);
        sidePanel.add(Box.createVerticalGlue());  // 用于拉伸剩余空间
//        sidePanel.add(separator);
        // 添加版本信息和设置按钮到侧边栏
        sidePanel.add(versionLabel);
        sidePanel.add(settingsLabel);

        return sidePanel;
    }

    private JLabel createNavLabel(String text, CardLayout cardLayout, JPanel mainPanel, String panelName) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));  // 设置字体样式，确保中文能正确显示
        label.setAlignmentX(Component.CENTER_ALIGNMENT);  // 标签居中显示
        label.setPreferredSize(new Dimension(200, 40));  // 设置标签的大小
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));  // 设置标签的上下边距
        label.setForeground(new Color(34, 34, 34));  // 设置文字颜色（深色）

        // 鼠标悬停效果
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));  // 设置为手型光标
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setForeground(new Color(0, 102, 204));  // 悬停时文字变蓝
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setForeground(new Color(34, 34, 34));  // 恢复原色
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, panelName);
            }
        });

        return label;
    }
}
