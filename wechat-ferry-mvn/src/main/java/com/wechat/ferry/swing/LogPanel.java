package com.wechat.ferry.swing;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class LogPanel {

    private static final String LOG_FILE_PATH = "logs/wechat-ferry/console.log"; // Log file path
    private static final int REFRESH_INTERVAL = 5000; // Refresh interval in milliseconds (5 seconds)
    private JTextArea logText; // To display logs

    public JPanel createLogPanel() throws UnsupportedEncodingException, FileNotFoundException {
        JPanel logPanel = new JPanel();
        logPanel.setBackground(Color.WHITE);
        logPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("日志输出", JLabel.CENTER);
        titleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));

        // Create JTextArea for displaying logs
        logText = new JTextArea();
        logText.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        logText.setEditable(false);
        logText.setBackground(Color.LIGHT_GRAY);
        logText.setLineWrap(true);
        logText.setWrapStyleWord(true);

        // Add JTextArea to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(logText);
        logPanel.add(titleLabel, BorderLayout.NORTH);
        logPanel.add(scrollPane, BorderLayout.CENTER);

        // Load initial logs and display them
        loadLogFromFile();

        // Set up the timer to refresh logs periodically
        startLogRefreshTimer();

        return logPanel;
    }

    private static final int MAX_LINES_TO_LOAD = 10;  // 最大加载的行数

    private void loadLogFromFile() {
        File logFile = new File(LOG_FILE_PATH);
        if (logFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(logFile), StandardCharsets.UTF_8))) {
                LinkedList<String> lines = new LinkedList<>();
                String line;
                logText.setText("");
                // 读取每一行并将其存储在链表中
                while ((line = reader.readLine()) != null) {
                    if (lines.size() >= MAX_LINES_TO_LOAD) {
                        lines.poll();  // 当达到最大行数时，移除最旧的一行
                    }
                    lines.add(line);  // 添加当前行到链表
                }

                // 显示最近的几行日志
                for (String logLine : lines) {
                    logText.append(logLine + "\n");
                }
                logText.setCaretPosition(logText.getDocument().getLength());  // 滚动到最后一行
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    // Start a timer to reload the log file periodically
    private void startLogRefreshTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                loadLogFromFile();  // Reload logs periodically
            }
        }, 0, REFRESH_INTERVAL);  // Initial delay = 0, period = REFRESH_INTERVAL (e.g., 5000ms)
    }
}
