package com.xbb;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.intellij.icons.AllIcons;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.xbb.constant.BondConstant;
import com.xbb.entity.JSLConvertibleBond;
import com.xbb.util.HttpClientPool;
import com.xbb.util.NumberField;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.xbb.constant.Constants.EMPTY_STR;

public class JiSiLuWindow {


    JPanel panel = new JPanel(new BorderLayout()); // main

    JTextField nameField = new JTextField(); // 名称
    JTextField low = new NumberField(); // 最低价
    JTextField high = new NumberField(); // 最高价
    JTextField premiumRateField = new NumberField(); // 溢价率
    JRadioButton jRadioButton = new JRadioButton("已上市");

    public JiSiLuWindow() {

        JBTable table = new JBTable();
        table.setFillsViewportHeight(true);
        JiSiLuModel model = new JiSiLuModel(JSLConvertibleBond.class);
        table.setModel(model);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        sorter.setComparator(model.findColumn(BondConstant.BOND_PRICE), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        sorter.setComparator(model.findColumn(BondConstant.BOND_INCREASE_RATE), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        sorter.setComparator(model.findColumn(BondConstant.STOCK_PRICE), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        sorter.setComparator(model.findColumn(BondConstant.STOCK_INCREASE_RATE), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        sorter.setComparator(model.findColumn(BondConstant.PREMIUM_RT), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        sorter.setComparator(model.findColumn(BondConstant.YTM_RT), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        sorter.setComparator(model.findColumn(BondConstant.DB_LOW), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        table.setRowSorter(sorter);


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

        JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
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
        JBScrollPane scrollPane = new JBScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
    }

    public static double applyAsDouble(Object o) {

        try {
            return Double.valueOf(o.toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @NotNull
    private ActionListener buildFilterActionListener(DefaultTableModel model, JTable table, List<RowFilter<Object, Object>> filters) {
        return l -> {
            buidFilter(filters, model, nameField, low, high, premiumRateField);
            if (jRadioButton.isSelected()) {
                filters.add(new RowFilter<>() {
                    @Override
                    public boolean include(Entry<? extends Object, ? extends Object> entry) {
                        return !entry.getValue(model.findColumn(BondConstant.BOND_PRICE)).equals("100");
                    }
                });
            }

            TableRowSorter rowSorter = (TableRowSorter) table.getRowSorter();
            rowSorter.setRowFilter(RowFilter.andFilter(filters));
            model.fireTableRowsUpdated(0, table.getRowCount() - 1);
        };
    }

    static void buidFilter(List<RowFilter<Object, Object>> filters, DefaultTableModel model, JTextField nameField, JTextField low, JTextField high,
                    JTextField premiumRateField) {
        filters.clear();
        String text = nameField.getText();
        if (StringUtils.isNotBlank(text)) {
            filters.add(RowFilter.regexFilter(text, model.findColumn(BondConstant.BOND_NAME),
                    model.findColumn(BondConstant.STOCK_NAME), model.findColumn(BondConstant.BOND_CODE)));
        }

        if (NumberUtils.isCreatable(low.getText())) {
            RowFilter<Object, Object> rowFilter = new RowFilter<>() {
                @Override
                public boolean include(Entry<? extends Object, ? extends Object> entry) {
                    Object value = entry.getValue(2);

                    if (NumberUtils.isCreatable(value.toString())) {
                        return Double.valueOf(value.toString()) > Double.valueOf(low.getText());
                    }
                    return false;
                }
            };
            filters.add(rowFilter);
        }
        if (NumberUtils.isCreatable(high.getText())) {
            RowFilter<Object, Object> rowFilter = new RowFilter<>() {
                @Override
                public boolean include(Entry<? extends Object, ? extends Object> entry) {
                    Object value = entry.getValue(2);
                    if (NumberUtils.isCreatable(value.toString())) {
                        return Double.valueOf(value.toString()) < Double.valueOf(high.getText());
                    }
                    return false;
                }
            };
            filters.add(rowFilter);
        }
        if (NumberUtils.isCreatable(premiumRateField.getText())) {
            RowFilter<Object, Object> rowFilter = new RowFilter<>() {
                @Override
                public boolean include(Entry<? extends Object, ? extends Object> entry) {
                    Object value = entry.getValue(model.findColumn(BondConstant.PREMIUM_RT));
                    if (StringUtils.isNotBlank(value.toString()) && NumberUtils.isCreatable(value.toString())) {
                        return Double.valueOf(value.toString()) < Double.valueOf(premiumRateField.getText());
                    }
                    return false;
                }
            };
            filters.add(rowFilter);
        }
    }

}
