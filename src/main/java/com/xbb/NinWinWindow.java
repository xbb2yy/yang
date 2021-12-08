package com.xbb;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionToolbarPosition;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.xbb.util.HttpClientPool;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NinWinWindow {


    JPanel panel;
    JTextField nameField;
    public NinWinWindow() {
        JBTable table = new JBTable();
        TableModel model = new NinWinTableModel(table);
        table.setModel(model);
        panel = new JPanel();
        JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setLayout(new BorderLayout());
        JLabel label = new JBLabel("价格区间");
        JTextField low = new JTextField();
        JLabel to = new JBLabel("到");
        JTextField high = new JTextField();
        jPanel.add(label);
        jPanel.add(low);
        jPanel.add(to);
        jPanel.add(high);
        JLabel name = new JLabel("\t名称");
        JButton searchBtn = new JButton("筛选");
        searchBtn.setIcon(AllIcons.Actions.Search);
        JButton stopRefreshBtn = new JButton();


        stopRefreshBtn.setIcon(AllIcons.Actions.StopRefresh);
        nameField = new JTextField();
        List<RowFilter<Object, Object>> filters = new ArrayList<>(2);
        searchBtn.addActionListener(l -> {
            filters.clear();
            String text = nameField.getText();
            if (StringUtils.isNotBlank(text)) {
                filters.add(RowFilter.regexFilter(text, 1));
            }

            if (NumberUtils.isDigits(low.getText())) {
                RowFilter<Object, Object> rowFilter = new RowFilter<>() {
                    @Override
                    public boolean include(Entry<? extends Object, ? extends Object> entry) {
                        Object value = entry.getValue(2);

                        if (NumberUtils.isDigits(value.toString())) {
                            return Double.valueOf(value.toString()) > Double.valueOf(low.getText());
                        }
                        return false;
                    }
                };
                filters.add(rowFilter);
            }
            if (NumberUtils.isDigits(high.getText())) {
                RowFilter<Object, Object> rowFilter = new RowFilter<>() {
                    @Override
                    public boolean include(Entry<? extends Object, ? extends Object> entry) {
                        Object value = entry.getValue(2);
                        if (NumberUtils.isDigits(value.toString())) {
                            return Double.valueOf(value.toString()) < Double.valueOf(low.getText());
                        }
                        return false;
                    }
                };
                filters.add(rowFilter);
            }
        });
        jPanel.add(name);
        jPanel.add(nameField);
        jPanel.add(searchBtn);
        jPanel.add(stopRefreshBtn);

        JBLabel marginRight = new JBLabel("指数:");
        JBLabel marginRightValue = new JBLabel("1937.0");
        jPanel.add(marginRight);
        jPanel.add(marginRightValue);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                String s = HttpClientPool.getHttpClient().get("https://www.jisilu.cn/webapi/cb/index_quote/");
                JsonElement jsonElement = JsonParser.parseString(s);
                String margin = jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("cur_increase_rt").getAsString();
                marginRightValue.setText(margin);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }, 0, 1, TimeUnit.SECONDS);

        panel.add(jPanel, BorderLayout.PAGE_START);
        table.setFillsViewportHeight(true);

        JBScrollPane scrollPane = new JBScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
    }
}
