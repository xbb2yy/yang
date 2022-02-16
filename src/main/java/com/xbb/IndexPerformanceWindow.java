package com.xbb;

import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.xbb.entity.Index;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class IndexPerformanceWindow {

    JPanel panel = new JPanel(new BorderLayout()); // main

    public IndexPerformanceWindow() {
        JBTable table = new JBTable();
        table.setFillsViewportHeight(true);
        IndexPerformance model = new IndexPerformance(Index.class);
        table.setModel(model);

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));


        JBScrollPane scrollPane = new JBScrollPane(table);
        panel.add(jPanel, BorderLayout.PAGE_START);
        panel.add(scrollPane, BorderLayout.CENTER);
    }
}
