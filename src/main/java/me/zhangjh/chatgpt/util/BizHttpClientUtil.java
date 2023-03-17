package me.zhangjh.chatgpt.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.zhangjh.chatgpt.socket.SocketServer;
import me.zhangjh.share.util.HttpClientUtil;
import me.zhangjh.share.util.HttpRequest;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @author zhangjh451@midea.com
 * @date 1:05 AM 2023/3/17
 * @Description
 */
@Slf4j
public class BizHttpClientUtil extends HttpClientUtil {

    public static String sendStream(HttpRequest httpRequest, SocketServer socketServer) {
        Request request = buildRequest(httpRequest);
        try (Response response = OK_HTTP_CLIENT.newCall(request).execute()){
            return handleResponse(httpRequest, Objects.requireNonNull(response.body()), socketServer);
        } catch (IOException e) {
            log.error("sendNormally exception, ", e);
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private static String handleResponse(HttpRequest httpRequest, ResponseBody responseBody,
                                         SocketServer socketServer) {
        StringBuilder fullMsg = new StringBuilder();
        try (responseBody){
            Map<String, String> bizHeaderMap = httpRequest.getBizHeaderMap();
            Assert.isTrue(MapUtils.isNotEmpty(bizHeaderMap) && StringUtils.isNotEmpty(bizHeaderMap.get("userId"))
                    , "userId为空");
            String content = responseBody.string();
            String[] resArr = content.split("\n\n");
            for (String res : resArr) {
                // if stream is true, send socket msg here
                // res format: data: {xxx}
                String partialMsg = socketServer.sendMessage(httpRequest.getBizHeaderMap().get("userId"),
                        res.substring(6));
                fullMsg.append(partialMsg);
            }
        }
        return fullMsg.toString();
    }
}
