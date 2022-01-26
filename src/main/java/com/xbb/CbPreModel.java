package com.xbb;

import com.google.gson.*;
import com.intellij.ide.util.AppPropertiesComponentImpl;
import com.intellij.ide.util.PropertiesComponent;
import com.xbb.constant.Constants;
import com.xbb.constant.URLConstants;
import com.xbb.entity.CbPre;
import com.xbb.table.JavaBeanTableModel;
import com.xbb.util.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 待发转债
 */
@Slf4j
public class CbPreModel extends JavaBeanTableModel<CbPre> {

    public CbPreModel(Class<CbPre> c) {
        super(c);
        if (LocalTime.now().isAfter(LocalTime.of(15, 05)) || LocalTime.now().isBefore(LocalTime.of(9, 10))) {
            updateData();
            return;
        }
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(() -> {
                    log.info("update data...");
                    updateData();
                }, 0, Constants.DATA_REFRESH_INTERVAL, TimeUnit.SECONDS);
    }

    private void updateData() {
        ArrayList<NameValuePair> objects = new ArrayList<>();
        objects.add(new BasicNameValuePair("progress", ""));
        objects.add(new BasicNameValuePair("rp", "22"));
        objects.add(new BasicNameValuePair("page", "1"));
        String result = DataUtil.getJSLData(URLConstants.PRE_LIST, objects);
        GsonBuilder gsonBuilder = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        JsonElement jsonElement = JsonParser.parseString(result);
        JsonArray rows = jsonElement.getAsJsonObject().get("rows").getAsJsonArray();
        for (JsonElement row : rows) {
            CbPre data = gsonBuilder.create().fromJson(row.getAsJsonObject().get("cell"), CbPre.class);
            if (data.getProgressNm().contains("申购"))
                data.setProgressNm(data.getProgressNm().substring(0, 12));
            insertEntityRow(data);
        }
    }
}
