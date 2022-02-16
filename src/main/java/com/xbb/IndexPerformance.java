package com.xbb;

import com.google.gson.*;
import com.xbb.constant.Constants;
import com.xbb.constant.URLConstants;
import com.xbb.entity.Index;
import com.xbb.table.JavaBeanTableModel;
import com.xbb.util.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 待发转债
 */
@Slf4j
public class IndexPerformance extends JavaBeanTableModel<Index> {

    public IndexPerformance(Class<Index> c) {
        super(c);
        if (LocalTime.now().isAfter(LocalTime.of(15, 5)) || LocalTime.now().isBefore(LocalTime.of(9, 10))) {
            updateData();
            return;
        }
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            log.info("update data...");
            updateData();
        }, 0, Constants.DATA_REFRESH_INTERVAL, TimeUnit.SECONDS);
    }

    private void updateData() {
        ArrayList<NameValuePair> objects = new ArrayList<>();
        String result = DataUtil.getJSLData(URLConstants.INDEX_PERFORMANCE, objects);
        GsonBuilder gsonBuilder = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        JsonElement jsonElement = JsonParser.parseString(result);
        JsonArray rows = jsonElement.getAsJsonObject().get("rows").getAsJsonArray();
        for (JsonElement row : rows) {
            Index data = gsonBuilder.create().fromJson(row.getAsJsonObject().get("cell"), Index.class);
            insertEntityRow(data);
        }
    }
}
