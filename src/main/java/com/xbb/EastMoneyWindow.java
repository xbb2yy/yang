package com.xbb;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.intellij.icons.AllIcons;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.xbb.constant.BondConstant;
import com.xbb.entity.EastMoneyConvertibleBond;
import com.xbb.util.HttpClientPool;
import com.xbb.util.NumberField;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EastMoneyWindow {

    private static final String EMPTY_STR = "";
    JPanel panel;
    JTextField nameField = new JTextField(); // 名称
    JTextField low = new NumberField(); // 最低价
    JTextField high = new NumberField(); // 最高价
    JTextField premiumRateField = new NumberField(); // 溢价率
    JRadioButton jRadioButton = new JRadioButton("已上市");

    public EastMoneyWindow() {
        JBTable table = new JBTable();
        EastMoneyTableModel model = new EastMoneyTableModel(EastMoneyConvertibleBond.class);
        table.setModel(model);

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        sorter.setComparator(model.findColumn(BondConstant.BOND_PRICE), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        sorter.setComparator(model.findColumn(BondConstant.BOND_INCREASE_RATE), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        sorter.setComparator(model.findColumn(BondConstant.STOCK_PRICE), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        sorter.setComparator(model.findColumn(BondConstant.STOCK_INCREASE_RATE), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        sorter.setComparator(model.findColumn(BondConstant.PREMIUM_RT), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        sorter.setComparator(model.findColumn(BondConstant.DB_LOW), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        table.setRowSorter(sorter);

        // 隐藏上市时间列
        TableColumn tc = table.getColumnModel().getColumn(9);
        table.getTableHeader().getColumnModel().getColumn(9).setMaxWidth(0);
        table.getTableHeader().getColumnModel().getColumn(9).setMinWidth(0);
        tc.setMaxWidth(0);
        tc.setPreferredWidth(0);
        tc.setMinWidth(0);
        tc.setWidth(0);

        panel = new JPanel();
        JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setLayout(new BorderLayout());
        JLabel label = new JBLabel("价格区间");
        JLabel to = new JBLabel("到");
        JLabel name = new JLabel("\t名称");
        JLabel premiumRate = new JBLabel("溢价率");
        JButton searchBtn = new JButton("筛选");
        searchBtn.setIcon(AllIcons.Actions.Search);
        JButton clear = new JButton("清空");


        clear.setIcon(AllIcons.Actions.Back);
        List<RowFilter<Object, Object>> filters = new ArrayList<>(2);
        searchBtn.addActionListener(buildFilterActionListener(model, table, filters));
        jRadioButton.addActionListener(buildFilterActionListener(model, table, filters));
        clear.addActionListener(e -> {
            low.setText(EMPTY_STR);
            high.setText(EMPTY_STR);
            nameField.setText(EMPTY_STR);
            premiumRateField.setText(EMPTY_STR);
            jRadioButton.setSelected(false);
            searchBtn.doClick();
        });

        jPanel.add(label);
        jPanel.add(low);
        jPanel.add(to);
        jPanel.add(high);
        jPanel.add(premiumRate);
        jPanel.add(premiumRateField);
        jPanel.add(name);
        jPanel.add(nameField);
        jPanel.add(jRadioButton);
        jPanel.add(searchBtn);
        jPanel.add(clear);


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

    @NotNull
    private ActionListener buildFilterActionListener(DefaultTableModel model, JTable table, List<RowFilter<Object, Object>> filters) {
        return l -> {
            JiSiLuWindow.buildFilter(filters, model, nameField, low, high, premiumRateField, null);
            if (jRadioButton.isSelected()) {
                filters.add(new RowFilter<>() {
                    @Override
                    public boolean include(Entry<? extends Object, ? extends Object> entry) {
                        return !entry.getValue(9).equals("-");
                    }
                });
            }

            TableRowSorter rowSorter = (TableRowSorter) table.getRowSorter();
            rowSorter.setRowFilter(RowFilter.andFilter(filters));
        };
    }
}
