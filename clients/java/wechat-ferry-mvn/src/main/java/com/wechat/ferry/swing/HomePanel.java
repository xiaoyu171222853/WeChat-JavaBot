package com.wechat.ferry.swing;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class HomePanel {

    public JPanel createHomePanel() {
        JPanel homePanel = new JPanel();
        homePanel.setBackground(Color.WHITE);
        homePanel.setLayout(new BorderLayout());

        // Title label
        JLabel titleLabel = new JLabel("欢迎使用若小智", JLabel.CENTER);
        titleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 130, 180));  // Title color

        // Description area
        JTextArea descriptionText = new JTextArea(
                "若小智是一款功能强大的微信机器人框架，\n" +
                        "提供自动回复等多种功能。\n" +
                        "你可以根据自己的需求开发插件来定制机器人。"
        );

        descriptionText.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        descriptionText.setEditable(false);
        descriptionText.setBackground(Color.WHITE);
        descriptionText.setLineWrap(true);
        descriptionText.setWrapStyleWord(true);
        descriptionText.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Scroll pane for the description
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionText);

        // Layout arrangement
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(descriptionScrollPane, BorderLayout.CENTER);

        // Adding all panels to home panel
        homePanel.add(topPanel, BorderLayout.NORTH);
        homePanel.add(bottomPanel, BorderLayout.CENTER);

        return homePanel;
    }
}