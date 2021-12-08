package com.xbb;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.xbb.constant.BondConstant;
import com.xbb.entity.CbPre;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Comparator;

public class CbPreWindow {

    JPanel panel = new JPanel(new BorderLayout()); // main

    public CbPreWindow() {
        JBTable table = new JBTable();
        table.setFillsViewportHeight(true);
        CbPreModel model = new CbPreModel(CbPre.class);
        table.setModel(model);

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        sorter.setComparator(model.findColumn(BondConstant.STOCK_PRICE), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        sorter.setComparator(model.findColumn(BondConstant.STOCK_INCREASE_RATE), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        sorter.setComparator(model.findColumn("转股价"), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        sorter.setComparator(model.findColumn("百元股票含权"), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        sorter.setComparator(model.findColumn("配售10张股数"), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        table.setRowSorter(sorter);

        JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JBLabel("方案进展");
        ComboBox<String> box = new ComboBox<>(new String[]{"所有", "董事会预案", "股东大会通过", "发审委通过", "证监会核准/同意注册"});
        jPanel.add(label);
        jPanel.add(box);

        box.addItemListener(i -> {
            if (i.getStateChange() == ItemEvent.SELECTED) {
                final String item = box.getItem();
                TableRowSorter rowSorter = (TableRowSorter) table.getRowSorter();
                rowSorter.setRowFilter(new RowFilter() {
                    @Override
                    public boolean include(Entry entry) {
                        if (item.equals("所有")) return true;
                        return entry.getValue(model.findColumn("进展")).equals(item);
                    }
                });
            }
        });

        JBScrollPane scrollPane = new JBScrollPane(table);
        panel.add(jPanel, BorderLayout.PAGE_START);
        panel.add(scrollPane, BorderLayout.CENTER);
    }
}
