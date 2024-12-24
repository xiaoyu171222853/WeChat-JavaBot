package com.wechat.ferry.swing;

import javax.swing.*;
import java.awt.*;

public class HomePanel {

    public JPanel createHomePanel() {
        JPanel homePanel = new JPanel();
        homePanel.setBackground(Color.WHITE);
        homePanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("首页内容", JLabel.CENTER);
        titleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));

        JTextArea homeText = new JTextArea("欢迎来到主界面！\n这里展示首页内容。\n可以在这里添加更多功能。");
        homeText.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        homeText.setEditable(false);
        homeText.setBackground(Color.LIGHT_GRAY);
        homeText.setLineWrap(true);
        homeText.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(homeText);
        homePanel.add(titleLabel, BorderLayout.NORTH);
        homePanel.add(scrollPane, BorderLayout.CENTER);

        return homePanel;
    }
}
