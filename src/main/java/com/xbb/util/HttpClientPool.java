package com.xbb.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class HttpClientPool {

    private static volatile HttpClientPool clientInstance;
    private HttpClient httpClient;

    public static HttpClientPool getHttpClient() {
        HttpClientPool tmp = clientInstance;
        if (tmp == null) {
            synchronized (HttpClientPool.class) {
                tmp = clientInstance;
                if (tmp == null) {
                    tmp = new HttpClientPool();
                    clientInstance = tmp;
                }
            }
        }

        return tmp;
    }

    private HttpClientPool() {
        buildHttpClient(null);
    }

    public void buildHttpClient(String proxyStr){
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(100, TimeUnit.SECONDS);
        connectionManager.setMaxTotal(200);// 连接池
        connectionManager.setDefaultMaxPerRoute(100);// 每条通道的并发连接数
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(2000).setSocketTimeout(2000).build();
        HttpClientBuilder httpClientBuilder = HttpClients.custom().setConnectionManager(connectionManager);
        if (proxyStr!=null && !proxyStr.isEmpty()){
            String[] s = proxyStr.split(":");
            if (s.length == 2){
                String host = s[0];
                int port = Integer.parseInt(s[1]);
                httpClientBuilder.setProxy(new HttpHost(host,port));
            }
        }
        httpClient =httpClientBuilder.setDefaultRequestConfig(requestConfig).build();
    }

    public String get(String url) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        return getResponseContent(url,httpGet);
    }

    public String post(String url) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        return getResponseContent(url, httpPost);
    }

    public String post(String url, Map<String, String> headers) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        headers.forEach((k, v) -> httpPost.addHeader(k, v));
        return getResponseContent(url, httpPost);
    }

    /**
     * 发送 application/x-www-form-urlencoded 请求
     * @param url 地址
     * @param headers 请求头部
     * @param params 参数
     * @return
     */
    public String postFormUrlEncoded(String url, Map<String, String> headers, List<NameValuePair> params) {
        HttpPost httpPost = new HttpPost(url);
        headers.forEach((k, v) -> httpPost.addHeader(k, v));
        String s = "";
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            s = EntityUtils.toString(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    private String getResponseContent(String url, HttpRequestBase request) throws Exception {
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
            String s = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            return s;
        } catch (Exception e) {
            throw new Exception("got an error from HTTP for url : " + URLDecoder.decode(url, "UTF-8"),e);
        } finally {
            if(response != null){
                EntityUtils.consumeQuietly(response.getEntity());
            }
            request.releaseConnection();
        }
    }

    public static void main(String[] args) {
        System.out.println(1);
    }
}