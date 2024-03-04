package me.zhangjh.chatgpt.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.zhangjh.chatgpt.client.ChatGptService;
import me.zhangjh.chatgpt.dto.request.ChatRequest;
import me.zhangjh.chatgpt.dto.request.ImageRequest;
import me.zhangjh.chatgpt.dto.request.TextRequest;
import me.zhangjh.chatgpt.dto.request.TranscriptionRequest;
import me.zhangjh.chatgpt.dto.response.ChatResponse;
import me.zhangjh.chatgpt.dto.response.ImageResponse;
import me.zhangjh.chatgpt.dto.response.TextResponse;
import me.zhangjh.chatgpt.dto.response.TranscriptionResponse;
import me.zhangjh.chatgpt.socket.SocketServer;
import me.zhangjh.chatgpt.util.BizHttpClientUtil;
import me.zhangjh.share.util.HttpClientUtil;
import me.zhangjh.share.util.HttpRequest;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author zhangjh
 * @date 3:21 PM 2022/12/15
 * @Description
 */
@Slf4j
public class ChatGptServiceImpl implements ChatGptService {

    @Value("${openai.apikey}")
    private String configApiKey;

    private String apiKey;

    private final Map<String, String> header = new HashMap<>();

    private static final String TEXT_COMPLETION_URL = "https://api.openai.com/v1/completions";

    private static final String IMAGE_GENERATE_URL = "https://api.openai.com/v1/images/generations";

    @Value("${openai.chat.url:https://api.openai.com/v1/chat/completions}")
    private String chatUrl;

    private static final String TRANSCRIPTION_URL = "https://api.openai.com/v1/audio/transcriptions";

    @PostConstruct
    public void init() {
        if(StringUtils.isEmpty(configApiKey)) {
            configApiKey = System.getenv("openai.apikey");
        }
        if(StringUtils.isEmpty(apiKey)) {
            apiKey = configApiKey;
        }
        Assert.isTrue(StringUtils.isNotEmpty(apiKey), "openai apiKey not exist");
        // openAi
        if(apiKey.startsWith("sk-")) {
            header.put("Authorization", "Bearer " + apiKey);
        } else {
            // azure
            header.put("api-key", apiKey);
        }
    }

    /**
     * allow set apiKey from outside, but you must define this bean yourself
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public TextResponse createTextCompletion(TextRequest textRequest, Map<String, String> bizParams) {
        // this interface must set request.stream to false
        textRequest.check();

        HttpRequest httpRequest = new HttpRequest(TEXT_COMPLETION_URL);
        httpRequest.setReqData(JSONObject.toJSONString(textRequest));
        httpRequest.setBizHeaderMap(this.header);
        String response = HttpClientUtil.sendNormally(httpRequest).toString();
        return JSONObject.parseObject(response, TextResponse.class);
    }

    @Override
    public ImageResponse createImageGeneration(ImageRequest imageRequest, Map<String, String> bizParams) {
        imageRequest.check();
        HttpRequest httpRequest = new HttpRequest(IMAGE_GENERATE_URL);
        httpRequest.setReqData(JSONObject.toJSONString(imageRequest));
        httpRequest.setBizHeaderMap(this.header);
        String response = HttpClientUtil.sendNormally(httpRequest).toString();
        return JSONObject.parseObject(response, ImageResponse.class);
    }

    @Override
    public ChatResponse createChatCompletion(ChatRequest request, Map<String, String> bizParams) {
        HttpRequest httpRequest = new HttpRequest(chatUrl);
        httpRequest.setReqData(JSONObject.toJSONString(request));
        httpRequest.setBizHeaderMap(this.header);
        String response = HttpClientUtil.sendNormally(httpRequest).toString();
        return JSONObject.parseObject(response, ChatResponse.class);
    }

    @Override
    public void createChatCompletionStream(ChatRequest request, Map<String, String> bizParams,
                                                 SocketServer socketServer, Function<String,
            Void> bizCb) {
        request.setStream(true);
        Assert.isTrue(MapUtils.isNotEmpty(bizParams) && StringUtils.isNotEmpty(bizParams.get("userId")),
                "userId为空");
        this.header.putAll(bizParams);
        HttpRequest httpRequest = new HttpRequest(chatUrl);
        httpRequest.setReqData(JSONObject.toJSONString(request));
        httpRequest.setBizHeaderMap(this.header);

        BizHttpClientUtil.sendStream(httpRequest, socketServer, bizCb);
    }

    @Override
    public TranscriptionResponse createTranscription(TranscriptionRequest request, Map<String, String> bizParams) {
        HttpRequest httpRequest = new HttpRequest(TRANSCRIPTION_URL);
        httpRequest.setReqData(JSONObject.toJSONString(request));
        httpRequest.setBizHeaderMap(this.header);
        String response = BizHttpClientUtil.sendFileMultiPart(httpRequest).toString();
        return JSONObject.parseObject(response, TranscriptionResponse.class);
    }
}
