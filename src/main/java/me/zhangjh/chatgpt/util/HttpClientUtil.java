package me.zhangjh.chatgpt.util;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.zhangjh.chatgpt.dto.response.BizException;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
            // there may be many content returned, set long socket time out
            .setSocketTimeout(500000)
            .setConnectionRequestTimeout(5000)
            .build();

    private static RequestConfig requestConfig = DEFAULT_REQUEST_CONFIG;

    /**
     * set specific config by user
     * */
    public static void setRequestConfig(RequestConfig config) {
        requestConfig = config;
    }

    /** 10 connections per domain
     *  100 total
     */
    private static final CloseableHttpClient HTTPCLIENT = HttpClients.custom()
            .setDefaultRequestConfig(requestConfig)
            .setMaxConnPerRoute(10)
            .setMaxConnTotal(100)
            .build();

    /**
     * @param url
     * @param body, json format request data
     */
    @Deprecated
    public static JSONObject sendHttp(String url, String body, Map<String, String> header) {
        HttpPost httpPost = new HttpPost(url);
        for (Map.Entry<String, String> entry : header.entrySet()) {
            httpPost.setHeader(entry.getKey(), entry.getValue());
        }
        httpPost.setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
        StringEntity entity = new StringEntity(body, Charset.defaultCharset());
        httpPost.setEntity(entity);
        try (CloseableHttpResponse response = HTTPCLIENT.execute(httpPost)) {
            String result = EntityUtils.toString(response.getEntity());
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return JSONObject.parseObject(result);
            } else {
                JSONObject jo = JSONObject.parseObject(result);
                throw new BizException(jo.getJSONObject("error").toString());
            }
        } catch (Throwable t) {
            log.error("sendHttp exception: ", t);
            throw new RuntimeException(t);
        }
    }

    public static JSONObject sendNormally(String url, String body, Map<String, String> headerMap) {
        return sendHttp(url, body, headerMap);
    }

    @SneakyThrows
    public static SseEmitter sendStream(String url, String body, Map<String, String> headerMap) {
        SseEmitter emitter = new SseEmitter(60L);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            httpPost.setHeader(entry.getKey(), entry.getValue());
        }
        StringEntity entity = new StringEntity(body, Charset.defaultCharset());
        httpPost.setEntity(entity);

        try (CloseableHttpResponse response = HTTPCLIENT.execute(httpPost)) {
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                try (InputStream inputStream = response.getEntity().getContent()) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        emitter.send(line);
                    }
                    emitter.complete();
                } catch (Throwable t) {
                    emitter.completeWithError(t);
                }
            }
        }
        HTTPCLIENT.close();

        return emitter;
    }
}
