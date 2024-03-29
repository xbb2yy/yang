package com.xbb.util;

import com.google.gson.*;
import com.intellij.ide.util.PropertiesComponent;
import com.xbb.constant.Constants;
import com.xbb.entity.BondDailyDetail;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据获取
 *
 * @author xbb
 */
public class DataUtil {


    public static String JSL_COOKIE = "";

    public static Gson gson = new Gson();
    static {
        try {
            JSL_COOKIE = PropertiesComponent.getInstance().getValue("JSL_COOKIE");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取集思录可转债数据
     * @param url
     * @param params
     * @return
     */
    public static String getJSLData(String url, List<NameValuePair> params) {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = getHttpPost(url);
        httpPost.addHeader("Cookie", JSL_COOKIE);

        String s = "";
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            HttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                s = EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonElement jsonElement = JsonParser.parseString(s);
        JsonArray rows = jsonElement.getAsJsonObject
                ().get("rows").getAsJsonArray();
        System.out.println("获取集思录数据，数据总条数:" + rows.size());
        return s;
    }

    @NotNull
    private static HttpPost getHttpPost(String url) {
        return new HttpPost(url);
    }

    /**
     * 登录集思录获取cookie
     *
     * @return
     * @throws Exception
     */
    public static String getJSLCookie() throws Exception {

        NashornScriptEngineFactory engineFactory = new NashornScriptEngineFactory();
        ScriptEngine engine = engineFactory.getScriptEngine();
        engine.eval(new InputStreamReader(DataUtil.class.getResourceAsStream("/jsl.js")));
        Invocable invocable = (Invocable) engine;
        ArrayList<NameValuePair> objects = new ArrayList<>();
        objects.add(new BasicNameValuePair("aes", "1"));
        objects.add(new BasicNameValuePair("return_url", "https://www.jisilu.cn/"));
        objects.add(new BasicNameValuePair("user_name", invocable.invokeFunction("jslencode", Constants.集思录用户名, "397151C04723421F").toString()));
        objects.add(new BasicNameValuePair("password", invocable.invokeFunction("jslencode",  Constants.集思录密码, "397151C04723421F").toString()));
        objects.add(new BasicNameValuePair("_post_type", "ajax"));
        objects.add(new BasicNameValuePair("net_auto_login", "1"));
        objects.add(new BasicNameValuePair("agreement_chk", "agree"));
        CookieStore httpCookieStore = new BasicCookieStore();
        CloseableHttpClient client = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore).build();
        HttpPost httpPost = getHttpPost("https://www.jisilu.cn/account/ajax/login_process/");
        StringBuilder builder = new StringBuilder();
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(objects, "UTF-8"));
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            HttpResponse response = client.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String s = EntityUtils.toString(entity);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                return null;
            }
            List<Cookie> cookies = httpCookieStore.getCookies();
            for (Cookie cookie : cookies) {
                builder.append(cookie.getName()).append("=").append(URLEncoder.encode(cookie.getValue(), StandardCharsets.UTF_8.displayName())).append("; ");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        String substring = builder.substring(0, builder.length() - 2);

        PropertiesComponent.getInstance().setValue("JSL_COOKIE", substring);
        JSL_COOKIE = substring;
        System.out.println("cookie: " + substring);
        return substring;
    }


    public static List<BondDailyDetail> getBondDetail(String url) {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = getHttpPost(url);
        httpPost.addHeader("Cookie", JSL_COOKIE);

        ArrayList<NameValuePair> objects = new ArrayList<>();
        objects.add(new BasicNameValuePair("page", "1"));
        objects.add(new BasicNameValuePair("rp", "50"));
        String s = "";
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(objects, "UTF-8"));
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            HttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                s = EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonElement jsonElement = JsonParser.parseString(s);
        JsonArray rows = jsonElement.getAsJsonObject
                ().get("rows").getAsJsonArray();
        List<BondDailyDetail> list = new ArrayList<>();
        GsonBuilder gsonBuilder = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        for (JsonElement row : rows) {
            JsonObject cell = row.getAsJsonObject().get("cell").getAsJsonObject();
            BondDailyDetail detail = gsonBuilder.create().fromJson(cell, BondDailyDetail.class);
            list.add(detail);
        }
        System.out.println("获取集思录数据，数据总条数:" + rows.size());
        return list;
    }

}
