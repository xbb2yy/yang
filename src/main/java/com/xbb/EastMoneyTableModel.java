package com.xbb;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.xbb.constant.Constants;
import com.xbb.constant.URLConstants;
import com.xbb.entity.EastMoneyConvertibleBond;
import com.xbb.table.JavaBeanTableModel;
import com.xbb.util.HttpClientPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 东方财富数据源
 */
@Slf4j
public class EastMoneyTableModel extends JavaBeanTableModel<EastMoneyConvertibleBond> {

    private static final Pattern compile = Pattern.compile("\\{.*}");
    private Gson gson = new Gson();

    public EastMoneyTableModel(Class<EastMoneyConvertibleBond> c) {
        super(c);
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            try {
                String result = HttpClientPool.getHttpClient().get(URLConstants.EAST_MONEY_URL);
                Matcher matcher = compile.matcher(result);
                if (!matcher.find()) {
                    return;
                }
                String resultJson = matcher.group();
                JsonElement jsonElement = JsonParser.parseString(resultJson);
                JsonArray rows = jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("diff").getAsJsonArray();
                log.info("获取东方财富数据，数据总条数:" + rows.size());
                for (JsonElement row : rows) {
                    EastMoneyConvertibleBond bond = gson.fromJson(row, EastMoneyConvertibleBond.class);
                    String cbPrice = bond.getF2();
                    String premiumRate = bond.getF237();
                    if (NumberUtils.isCreatable(cbPrice) && NumberUtils.isCreatable(premiumRate)) {
                        double dBlow = Double.valueOf(cbPrice) + Double.valueOf(premiumRate);
                        bond.setDBlow(new BigDecimal(dBlow).setScale(3, RoundingMode.HALF_UP).toString()); // 双低
                    } else {
                        bond.setDBlow("0");
                    }
                    insertEntityRow(bond);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
