package com.xbb;

import com.google.gson.*;
import com.xbb.constant.Constants;
import com.xbb.constant.URLConstants;
import com.xbb.entity.BondDailyDetail;
import com.xbb.entity.JSLConvertibleBond;
import com.xbb.table.JavaBeanTableModel;
import com.xbb.util.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class BondModel extends JavaBeanTableModel<BondDailyDetail> {

    public static Map<String, String> data = new HashMap<>();

    public static final Map<String, JSLConvertibleBond> bonds = new HashMap<>();
    private static volatile boolean init = false;
    public BondModel(Class<BondDailyDetail> c) {
        super(c);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
