package com.wechat.ferry.swing;

import org.yaml.snakeyaml.Yaml;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

public class ConfigWindow {
    private JFrame frame;
    private JTextField dllField;
    private JTextField ruojyTokenField;
    private JTextField pluginField;
    private JButton continueButton;
    private JLabel downloadLabel;
    private JLabel explainLabel;
    private File configFile;
    private Runnable onContinue;  // 配置完成后的回调
    private static SwingWorker<Void, Integer> downloadWorker = null;  // 保存下载任务的引用

    public ConfigWindow(File configFile, Runnable onContinue) {
        this.configFile = configFile;
        this.onContinue = onContinue;
        initialize();
    }

    private void initialize() {
        // 初始化框架
        frame = new JFrame("配置窗口");
        frame.setSize(600, 400);
        frame.setMinimumSize(new Dimension(600, 400));
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);  // 禁用默认关闭操作
        frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);  // 窗口居中显示
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);  // 增加内边距，使界面更舒适

        // 标签和字段
        JLabel dllLabel = new JLabel("dll路径:");
        dllField = new JTextField();
        JLabel tokenLabel = new JLabel("若智云密钥：");
        ruojyTokenField = new JTextField();

        pluginField = new JTextField();
        continueButton = new JButton("继续");
        explainLabel = new JLabel("微信版本要求：3.9.12.17");

        // 设置下载链接
        downloadLabel = new JLabel("<html><a href='#'>点击这里下载对应版本微信</a></html>");
        downloadLabel.setForeground(Color.BLUE);
        downloadLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        downloadLabel.setPreferredSize(new Dimension(300, 30));  // 设置适当的首选大小
        downloadLabel.setMaximumSize(new Dimension(300, 30));    // 设置最大宽度

        // 设置文本框的最小宽度和首选宽度
        dllField.setMinimumSize(new Dimension(250, 40));
        dllField.setPreferredSize(new Dimension(400, 40));
        ruojyTokenField.setMinimumSize(new Dimension(250, 40));
        ruojyTokenField.setPreferredSize(new Dimension(400, 40));

        // 创建按钮面板，按钮居中
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(continueButton);

        // 布局组件
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        frame.add(dllLabel, gbc);

        gbc.gridx = 1;
        frame.add(dllField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(tokenLabel, gbc);

        gbc.gridx = 1;
        frame.add(ruojyTokenField, gbc);

        // 按钮居中，位于第3行（与其他输入框分开）
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;  // 让按钮横跨两列
        frame.add(buttonPanel, gbc);

        // 微信版本说明
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(explainLabel, gbc);

        // 下载链接
        gbc.gridy = 5;
        frame.add(downloadLabel, gbc);  // 添加下载链接

        // 加载配置
        loadConfig();

        // 显示窗口
        frame.setVisible(true);

        // 按钮点击事件
        continueButton.addActionListener(e -> {
            saveConfig();  // 保存配置
            if (onContinue != null) {
                frame.dispose();  // 关闭配置窗口
                // 创建主界面，跳转到主界面类
                try {
                    onContinue.run();  // 执行回调启动 Spring Boot
                    MainInterface mainInterface = new MainInterface();
                    mainInterface.showMainInterface();
                } catch (Exception ex) {
                    showError(ex.getMessage());
                }

            }
        });

        // 为下载链接添加鼠标点击事件
        downloadLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                startDownload();
            }
        });

        // 监听窗口关闭事件，取消下载任务
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (downloadWorker != null && !downloadWorker.isDone()) {
                    int option = JOptionPane.showConfirmDialog(frame,
                            "下载任务正在进行，是否取消并退出?",
                            "确认退出",
                            JOptionPane.YES_NO_OPTION);

                    if (option == JOptionPane.YES_OPTION) {
                        downloadWorker.cancel(true);  // 取消下载任务
                        frame.dispose();  // 关闭窗口
                    }
                } else {
                    frame.dispose();  // 如果没有下载任务正在进行，直接关闭窗口
                }
            }
        });
    }


    // 加载配置文件中的信息
    private void loadConfig() {
        Yaml yaml = new Yaml();
        try (InputStream input = new FileInputStream(configFile)) {
            Map<String, Object> config = yaml.load(input);
            if (config == null) {
                showError("配置文件加载失败，返回null");
                return;
            }

            // 获取配置项
            String dllPath = (String) getNestedValue(config, "wechat.ferry.dll-path");
            String token = (String) getNestedValue(config, "ruojy.token");
            String plugin = (String) getNestedValue(config, "ruojy.plugin");

            dllField.setText(dllPath != null ? dllPath : "");
            ruojyTokenField.setText(token != null ? token : "");
            pluginField.setText(plugin != null ? plugin : "");

        } catch (IOException e) {
            showError("配置加载失败: " + e.getMessage());
        }
    }

    // 保存配置到文件
    private void saveConfig() {
        String ruojyTokenFieldText = ruojyTokenField.getText();
        String dllFieldText = dllField.getText().replace("\\", "\\\\");
        String pluginFieldText =  pluginField.getText().replace("\\", "\\\\");
        try {
            String configContent = new String(Files.readAllBytes(configFile.toPath()), StandardCharsets.UTF_8);
            String newConfigContent = configContent
                    .replaceAll("(?m)^(\\s*)token:\\s*\\S+", "$1token: " + ruojyTokenFieldText)
                    .replaceAll("(?m)^(\\s*)dll-path:\\s*\\S+", "$1dll-path: " + dllFieldText)
                    .replaceAll("(?m)^(\\s*)ruojy:\\s*plugin:\\s*\\S*", "$1ruojy:\n  plugin: " + pluginFieldText);

            Files.write(configFile.toPath(), newConfigContent.getBytes(StandardCharsets.UTF_8));
            JOptionPane.showMessageDialog(frame, "配置已保存！");
        } catch (IOException e) {
            showError("保存配置失败: " + e.getMessage());
        }
    }

    // 辅助方法：从嵌套的Map中获取值
    private Object getNestedValue(Map<String, Object> map, String key) {
        String[] keys = key.split("\\.");
        Object value = map;
        for (String k : keys) {
            if (value instanceof Map) {
                value = ((Map<?, ?>) value).get(k);
            } else {
                return null;
            }
        }
        return value;
    }

    // 显示错误信息
    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "错误", JOptionPane.ERROR_MESSAGE);
    }

    // 启动下载任务
    private void startDownload() {
        try {
            // 指定网址
            URI uri = new URI("https://www.123865.com/s/QqyDVv-9qj0h");

            // 判断是否支持桌面操作
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                // 打开浏览器
                desktop.browse(uri);
            } else {
                System.out.println("Desktop is not supported on this system.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
