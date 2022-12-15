package me.zhangjh.chatgpt.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import wiremock.org.apache.http.HttpResponse;
import wiremock.org.apache.http.HttpStatus;
import wiremock.org.apache.http.client.config.RequestConfig;
import wiremock.org.apache.http.client.methods.HttpPost;
import wiremock.org.apache.http.entity.ContentType;
import wiremock.org.apache.http.entity.StringEntity;
import wiremock.org.apache.http.impl.client.CloseableHttpClient;
import wiremock.org.apache.http.impl.client.HttpClients;
import wiremock.org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author zhangjh
 * @date 2022/12/15
 * @Description
 */
@Slf4j
public class HttpClientUtil {
    private static final RequestConfig DEFAULT_REQUEST_CONFIG = RequestConfig.custom()
            .setConnectTimeout(5000)
            .setSocketTimeout(5000)
            .setConnectionRequestTimeout(5000)
            .build();

    /** 10 connections per domain
     *  100 total
     */
    private static final CloseableHttpClient HTTPCLIENT = HttpClients.custom()
            .setDefaultRequestConfig(DEFAULT_REQUEST_CONFIG)
            .setMaxConnPerRoute(10)
            .setMaxConnTotal(100)
            .build();

    /**
     * @param url
     * @param body, json format request data
     */
    public static JSONObject sendHttp(String url, String body, Map<String, String> header) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        for (Map.Entry<String, String> entry : header.entrySet()) {
            httpPost.setHeader(entry.getKey(), entry.getValue());
        }
        httpPost.setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
        StringEntity entity = new StringEntity(body, Charset.defaultCharset());
        httpPost.setEntity(entity);
        HttpResponse response = null;
        try {
            response = HTTPCLIENT.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity());
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return JSONObject.parseObject(result);
            } else {
                JSONObject jo = JSONObject.parseObject(result);
                jo.put("errorMsg", jo.get("error"));
                return jo;
            }
        } catch (Throwable t) {
            log.error("sendHttp exception: ", t);
            throw new RuntimeException(t);
        }
        finally {
            if(null != response) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException ignored) {
                }
            }
        }
    }
}
