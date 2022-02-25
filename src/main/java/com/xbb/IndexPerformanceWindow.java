package com.xbb;

import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.xbb.constant.BondConstant;
import com.xbb.entity.Index;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.Comparator;

public class IndexPerformanceWindow {

    JPanel panel = new JPanel(new BorderLayout()); // main

    public IndexPerformanceWindow() {
        JBTable table = new JBTable();
        table.setFillsViewportHeight(true);
        IndexPerformance model = new IndexPerformance(Index.class);
        table.setModel(model);

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        sorter.setComparator(model.findColumn(Index.现价), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        sorter.setComparator(model.findColumn(Index.涨幅), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        sorter.setComparator(model.findColumn(Index.本周涨幅), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        sorter.setComparator(model.findColumn(Index.本月涨幅), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        sorter.setComparator(model.findColumn(Index.本季涨幅), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        sorter.setComparator(model.findColumn(Index.本年涨幅), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        sorter.setComparator(model.findColumn(Index.涨幅2021), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        sorter.setComparator(model.findColumn(Index.涨幅2020), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        sorter.setComparator(model.findColumn(Index.涨幅2019), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        sorter.setComparator(model.findColumn(Index.涨幅2018), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        sorter.setComparator(model.findColumn(Index.涨幅2017), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        sorter.setComparator(model.findColumn(Index.涨幅2016), Comparator.comparingDouble(JiSiLuWindow::applyAsDouble));
        table.setRowSorter(sorter);

        JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));


        JBScrollPane scrollPane = new JBScrollPane(table);
        panel.add(jPanel, BorderLayout.PAGE_START);
        panel.add(scrollPane, BorderLayout.CENTER);
    }
}
