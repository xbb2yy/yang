package com.xbb;

import com.google.gson.*;
import com.xbb.constant.Constants;
import com.xbb.constant.URLConstants;
import com.xbb.entity.JSLConvertibleBond;
import com.xbb.table.JavaBeanTableModel;
import com.xbb.util.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class JiSiLuModel extends JavaBeanTableModel<JSLConvertibleBond> {

    public JiSiLuModel(Class<JSLConvertibleBond> c) {
        super(c);
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(() -> {
                    log.info("update data...");
                    ArrayList<NameValuePair> objects = new ArrayList<>();
                    objects.add(new BasicNameValuePair("fprice", ""));
                    objects.add(new BasicNameValuePair("tprice", ""));
                    objects.add(new BasicNameValuePair("curr_iss_amt", ""));
                    objects.add(new BasicNameValuePair("volume", ""));
                    objects.add(new BasicNameValuePair("svolume", ""));
                    objects.add(new BasicNameValuePair("premium_rt", ""));
                    objects.add(new BasicNameValuePair("ytm_rt", ""));
                    objects.add(new BasicNameValuePair("rating_cd", ""));
                    objects.add(new BasicNameValuePair("is_search", "N"));
                    objects.add(new BasicNameValuePair("market_cd[]", "shmb"));
                    objects.add(new BasicNameValuePair("market_cd[]", "shkc"));
                    objects.add(new BasicNameValuePair("market_cd[]", "szmb"));
                    objects.add(new BasicNameValuePair("market_cd[]", "szcy"));
                    objects.add(new BasicNameValuePair("btype", ""));
                    objects.add(new BasicNameValuePair("listed", "N"));
                    objects.add(new BasicNameValuePair("qflag", "N"));
                    objects.add(new BasicNameValuePair("sw_cd", ""));
                    objects.add(new BasicNameValuePair("bond_ids", ""));
                    objects.add(new BasicNameValuePair("rp", "50"));
                    String result = DataUtil.getJSLData(URLConstants.JSL_URL, objects);
                    GsonBuilder gsonBuilder = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
                    JsonElement jsonElement = JsonParser.parseString(result);
                    JsonArray rows = jsonElement.getAsJsonObject().get("rows").getAsJsonArray();
                    for (JsonElement row : rows) {
                        JSLConvertibleBond bond = gsonBuilder.create().fromJson(row.getAsJsonObject().get("cell"), JSLConvertibleBond.class);
                        insertEntityRow(bond);
                    }
                }, 0, Constants.DATA_REFRESH_INTERVAL, TimeUnit.SECONDS);
    }
}
