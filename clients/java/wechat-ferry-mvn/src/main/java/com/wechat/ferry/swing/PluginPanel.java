package com.wechat.ferry.swing;

import com.wechat.ferry.entity.PluginClass;
import com.wechat.ferry.entity.PluginPackage;
import com.wechat.ferry.plugin.ChatBotPlugin;
import com.wechat.ferry.plugin.PluginManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PluginPanel {

    // 创建插件数据
    public JPanel createPluginPanel() {
        JPanel pluginPanel = new JPanel();
        pluginPanel.setBackground(Color.WHITE);
        pluginPanel.setLayout(new BorderLayout());  // 增加组件间的间距

        // 设置标题
        JLabel titleLabel = new JLabel("插件列表", JLabel.CENTER);
        titleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));  // 加大字体
        titleLabel.setForeground(new Color(70, 130, 180));  // Title color
        // 将标题添加到顶部
        pluginPanel.add(titleLabel, BorderLayout.NORTH);

        List<PluginPackage> pluginPackages =new ArrayList<>();
        PluginManager.startedPlugins.forEach(plugin -> {

            // 获取插件的名称、描述、版本、作者
            String pluginName = plugin.getClass().getSimpleName();
            String pluginDescription = plugin.getPluginDescription();
            String pluginVersion = plugin.getPluginVersion();
            String pluginAuthor = plugin.getDeveloperName();

            // 构建 PluginClass 对象
            PluginClass pluginClassObj = new PluginClass(pluginName, pluginDescription, pluginVersion, pluginAuthor,plugin);

            // 构建 PluginPackage 对象
            PluginPackage pluginPackage = new PluginPackage(plugin.getClass().getSimpleName(), Arrays.asList(pluginClassObj));
            pluginPackages.add(pluginPackage);
        });

        // 计算所有插件包中的类总数
        int rowCount = 0;
        for (PluginPackage pluginPackage : pluginPackages) {
            rowCount += pluginPackage.getClasses().size();
        }

        // 创建表格列的标题
        String[] columnNames = { "类名", "描述", "版本", "作者", "插件"};


        // 创建表格数据，依赖于 `createPluginData` 方法
        Object[][] data = new Object[rowCount][5]; // 设置表格的行数

        int rowIndex = 0;
        for (PluginPackage pluginPackage : pluginPackages) {
            for (PluginClass pluginClass : pluginPackage.getClasses()) {
                data[rowIndex][0] = pluginClass.getClassName();
                data[rowIndex][1] = pluginClass.getDescription();
                data[rowIndex][2] = pluginClass.getVersion();
                data[rowIndex][3] = pluginClass.getAuthor();
                data[rowIndex][4] = pluginClass.getPlugin();
                rowIndex++;
            }
        }

        // 创建表格
        JTable pluginTable = new JTable(data, columnNames);
        pluginTable.setFillsViewportHeight(true);
        pluginTable.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));

        // 设置表格的行高
        pluginTable.setRowHeight(40);  // 修改行高，40像素

        // 修改列标题行的高度
        TableColumnModel columnModel = pluginTable.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setHeaderRenderer(new HeaderRenderer(30)); // 设置列标题的高度为 30 像素
        }

        // 设置所有列的内容居中
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);  // 设置居中对齐

        for (int i = 0; i < pluginTable.getColumnCount(); i++) {
            pluginTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // 创建右键菜单
        JPopupMenu rightClickMenu = new JPopupMenu();
        JMenuItem enableMenuItem = new JMenuItem("启用");
        JMenuItem disableMenuItem = new JMenuItem("禁用");
        // 启用菜单项点击事件
        enableMenuItem.addActionListener(e -> {
            int selectedRow = pluginTable.getSelectedRow();
            if (selectedRow != -1) {
                ChatBotPlugin plugin = (ChatBotPlugin) pluginTable.getValueAt(selectedRow, 4);
                PluginManager.startPlugin(plugin);
            }
        });

        // 禁用菜单项点击事件
        disableMenuItem.addActionListener(e -> {
            int selectedRow = pluginTable.getSelectedRow();
            if (selectedRow != -1) {
                ChatBotPlugin plugin = (ChatBotPlugin) pluginTable.getValueAt(selectedRow, 4);
                PluginManager.destroyPlugin(plugin);
            }
        });

        // 将菜单项添加到右键菜单
        rightClickMenu.add(enableMenuItem);
        rightClickMenu.add(disableMenuItem);

        // 为表格添加右键点击事件监听器
        pluginTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = pluginTable.rowAtPoint(e.getPoint());
                    int column = pluginTable.columnAtPoint(e.getPoint());
                    if (row >= 0 && column >= 0) {
                        pluginTable.setRowSelectionInterval(row, row);
                        pluginTable.setColumnSelectionInterval(column, column);

                        // 获取选中的插件
                        ChatBotPlugin plugin = (ChatBotPlugin) pluginTable.getValueAt(row, 4);
                        // 判断插件是否在 startedPlugins 或 destroyedPlugins 中
                        if (PluginManager.startedPlugins.contains(plugin)) {
                            // 插件在 startedPlugins 中，显示禁用菜单项
                            enableMenuItem.setEnabled(false);  // 禁用启用选项
                            disableMenuItem.setEnabled(true);  // 启用禁用选项
                        } else if (PluginManager.destroyedPlugins.contains(plugin)) {
                            // 插件在 destroyedPlugins 中，显示启用菜单项
                            enableMenuItem.setEnabled(true);  // 启用启用选项
                            disableMenuItem.setEnabled(false);  // 禁用禁用选项
                        }

                        rightClickMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });


        // 添加表格到滚动面板
        JScrollPane scrollPane = new JScrollPane(pluginTable);
        pluginPanel.add(scrollPane, BorderLayout.CENTER);

        // 为插件表格添加选择事件
        pluginTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = pluginTable.getSelectedRow();
                if (selectedRow != -1) {
                    // 获取选中插件的详细信息
                    String pluginName = (String) pluginTable.getValueAt(selectedRow, 0);
                    String pluginDescription = (String) pluginTable.getValueAt(selectedRow, 1);
                    String pluginVersion = (String) pluginTable.getValueAt(selectedRow, 2);
                    String pluginAuthor = (String) pluginTable.getValueAt(selectedRow, 3);
                    // 这里可以根据选中的插件做更多操作
                    System.out.println("选中了插件: " + pluginName);
                    System.out.println("描述: " + pluginDescription);
                    System.out.println("版本: " + pluginVersion);
                    System.out.println("作者: " + pluginAuthor);
                }
            }
        });

        return pluginPanel;
    }

    // 自定义表头渲染器，修改列标题行高
    private class HeaderRenderer extends JLabel implements TableCellRenderer {
        private int height;

        public HeaderRenderer(int height) {
            this.height = height;
            setHorizontalAlignment(JLabel.CENTER);
            setFont(new Font("Microsoft YaHei", Font.BOLD, 16));  // 设置列标题字体
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "");
            return this;
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension preferredSize = super.getPreferredSize();
            preferredSize.height = height; // 设置行高
            return preferredSize;  // 返回 Dimension 类型
        }
    }
}
