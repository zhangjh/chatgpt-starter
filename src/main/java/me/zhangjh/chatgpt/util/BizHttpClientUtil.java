package me.zhangjh.chatgpt.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.zhangjh.chatgpt.dto.request.TranscriptionRequest;
import me.zhangjh.chatgpt.dto.response.ChatResponse;
import me.zhangjh.chatgpt.dto.response.ChatRet;
import me.zhangjh.chatgpt.dto.response.ChatStreamRet;
import me.zhangjh.chatgpt.socket.SocketServer;
import me.zhangjh.share.util.HttpClientUtil;
import me.zhangjh.share.util.HttpRequest;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author njhxzhangjihong@126.com
 * @date 1:05 AM 2023/3/17
 * @Description
 */
@Slf4j
public class BizHttpClientUtil extends HttpClientUtil {

    private static HashMap<String, StringBuilder> msgMap = new HashMap<>();

    private static final String FINISH_FLAG = "[DONE]";

    public static void sendStream(HttpRequest httpRequest, SocketServer socketServer, Function<String, Void> bizCb) {
        EventSource.Factory factory = EventSources.createFactory(OK_HTTP_CLIENT);
        EventSourceListener eventSourceListener = new EventSourceListener() {
            @Override
            public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
                log.info("id: {}, type: {}, data: {}", id, type, data);
                // 异步结果回来后要从eventSource里取userId，因为不确定返回事件里的结果是哪次请求的
                String userId = eventSource.request().header("userId");
                handleResponse(userId, data, socketServer, bizCb);
            }

            @Override
            public void onFailure(@NotNull EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
                handleException(t);
            }
        };
        factory.newEventSource(buildRequest(httpRequest), eventSourceListener);
    }

    public static Object sendFileMultiPart(HttpRequest request) {
        TranscriptionRequest transcriptionRequest = JSONObject.parseObject(request.getReqData(), TranscriptionRequest.class);

        MediaType mediaType = MediaType.parse("multipart/form-data");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", transcriptionRequest.getFile(),
                        RequestBody.create(new File(transcriptionRequest.getFile()), mediaType))
                .addFormDataPart("model", transcriptionRequest.getModel())
                .build();
        Request httpRequest = new Request.Builder()
                .url(request.getUrl())
                .method("POST", body)
                .addHeader("Authorization", request.getBizHeaderMap().get("Authorization"))
                .addHeader("Content-Type", "multipart/form-data")
                .build();
        try (Response response = OK_HTTP_CLIENT.newCall(httpRequest).execute()){
            return handleResponse(Objects.requireNonNull(response.body()));
        } catch (IOException e) {
            log.error("sendFileMultiPart exception, ", e);
            throw new RuntimeException(e);
        }
    }

    private static void handleResponse(String userId, String data,
                                         SocketServer socketServer, Function<String, Void> bizCb) {
        Assert.isTrue(StringUtils.isNotEmpty(userId), "userId为空");
        // if stream is true, send socket msg here
        // res format: data: {xxx}
        if(StringUtils.isNotEmpty(data)) {
            if(data.equals(FINISH_FLAG)) {
                socketServer.sendMessage(userId, data);
                String fullMsg = msgMap.get(userId).toString();
                bizCb.apply(fullMsg);
                // 清空缓存
                msgMap.remove(userId);
                return;
            }
            ChatResponse chatResponse = JSONObject.parseObject(data, ChatResponse.class);
            List<ChatRet> choices = chatResponse.getChoices();
            StringBuilder partialMsg = msgMap.getOrDefault(userId, new StringBuilder());

            for (ChatRet choice : choices) {
                List<ChatStreamRet> delta = choice.getDelta();
                for (ChatStreamRet ret : delta) {
                    if(ret == null) {
                        continue;
                    }
                    String content = ret.getContent();
                    if(StringUtils.isNotEmpty(content)) {
                        socketServer.sendMessage(userId, content);
                        partialMsg.append(ret.getContent());
                    }
                }
            }
            msgMap.put(userId, partialMsg);
        }
    }

    private static String handleException(Throwable t) {
        log.error("sendStream exception:", t);
        return null;
    }
}
