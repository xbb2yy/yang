package com.xbb;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class NinWinTableModel extends DefaultTableModel {

    public static final String[] headers = {"代码", "转债名称", "现价", "涨跌幅", "正股名称", "正股价", "正股涨跌", "溢价率", "到期收益率", "双低"};

    public NinWinTableModel(JTable jTable) {
        jTable.setModel(this);
        setColumnIdentifiers(headers);
        Executors.newScheduledThreadPool(1)
                .scheduleAtFixedRate(() -> {
                    URL url;
                    try {
                        url = new URL("https://www.ninwin.cn/index.php?m=cb&show_cb_only=Y&show_listed_only=Y");
                        log.info("数据刷新");
                        Document document = Jsoup.parse(url, 10 * 1000);
                        Elements trs = document.select("tbody tr");
                        for (Element tr : trs) {
                            if (tr.id().equals("th_row")) continue;
                            Vector<String> s = new Vector<>();
                            s.add(tr.attr("data-cbcode"));
                            s.add(tr.attr("data-cb_name"));
                            s.add(tr.attr("data-cb_price"));
                            s.add(tr.select("td.cb_mov2_id").first().select("spand").text());
                            s.add(tr.attr("data-cb_name"));
                            s.add(tr.select("td.stock_price_id").text());
                            s.add(tr.select("td.cb_mov_id").first().select("spand").text());
                            s.add(tr.select("td.cb_premium_id").text());
                            s.add(tr.select("td.cb_premium_id").text());
                            s.add(tr.select("td.cb_premium_id").text());
                            updateData(s);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }, 0, 3, TimeUnit.HOURS);

    }

    private void updateData(Vector<String> data) {
        for (int i = 0; i < getRowCount(); i++) {
            Object valueAt = getValueAt(i, 0);
            if (StringUtils.equals(data.get(0), valueAt.toString())) {
                dataVector.set(i, data);
                fireTableRowsUpdated(0, getRowCount() - 1);
                return;
            }
        }
        addRow(data);
    }

}
