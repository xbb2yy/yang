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

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class JiSiLuModel extends JavaBeanTableModel<JSLConvertibleBond> {

    public static Map<String, String> data = new HashMap<>();

    public static final Map<String, JSLConvertibleBond> bonds = new HashMap<>();
    private static final ScheduledExecutorService scheduledExecutorService =  Executors.newSingleThreadScheduledExecutor();
    private static boolean init = false;
    public JiSiLuModel(Class<JSLConvertibleBond> c) {
        super(c);
        scheduledExecutorService
                .scheduleAtFixedRate(() -> {
                    log.info("update data...");
                    if (LocalTime.now().isAfter(LocalTime.of(15, 5)) || LocalTime.now().isBefore(LocalTime.of(9, 10))) {
                        if (init) {
                            return;
                        }
                    }
                    updateData();
                    init = true;
                }, 0, Constants.DATA_REFRESH_INTERVAL, TimeUnit.SECONDS);
    }

    public static void reFresh() {
        init = false;
    }


    private void updateData() {
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
            bonds.put(bond.getBondId(), bond);
            data.put(row.getAsJsonObject().get("id").getAsString(), row.getAsJsonObject().get("cell").toString());
            insertEntityRow(bond);
        }
    }
}
