package me.zhangjh.chatgpt.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.zhangjh.chatgpt.client.ChatGptService;
import me.zhangjh.chatgpt.dto.request.ChatRequest;
import me.zhangjh.chatgpt.dto.request.ImageRequest;
import me.zhangjh.chatgpt.dto.request.TextRequest;
import me.zhangjh.chatgpt.dto.response.ChatResponse;
import me.zhangjh.chatgpt.dto.response.ImageResponse;
import me.zhangjh.chatgpt.dto.response.TextResponse;
import me.zhangjh.chatgpt.socket.SocketServer;
import me.zhangjh.chatgpt.util.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

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

    private static final String CHAT_COMPLETION_URL = "https://api.openai.com/v1/chat/completions";

    @Autowired
    private SocketServer socketServer;

    @PostConstruct
    public void init() {
        if(StringUtils.isEmpty(configApiKey)) {
            configApiKey = System.getenv("openai.apikey");
        }
        if(StringUtils.isEmpty(apiKey)) {
            apiKey = configApiKey;
        }
        Assert.isTrue(StringUtils.isNotEmpty(apiKey), "openai apiKey not exist");
        header.put("Authorization", "Bearer " + apiKey);
    }

    /**
     * allow set apiKey from outside, but you must define this bean yourself
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public void setHeader(Map<String, String> headers) {
        this.header.putAll(headers);
    }

    @Override
    public TextResponse createTextCompletion(TextRequest textRequest) {
        // this interface must set request.stream to false
        TextResponse response;
        try {
            Assert.isTrue(!textRequest.getStream(), "use createTextCompletionStream to get stream support");
            JSONObject jsonObject = HttpClientUtil.sendNormally(TEXT_COMPLETION_URL, JSONObject.toJSONString(textRequest),
                    header);
            response = JSONObject.parseObject(jsonObject.toString(), TextResponse.class);
        } catch (Throwable t) {
            log.error("createCompletion failed, data: {}, t: ",
                    JSONObject.toJSONString(textRequest), t);
            throw new RuntimeException(t.getCause());
        }
        return response;
    }

    @Override
    public SseEmitter createTextCompletionStream(TextRequest textRequest) {
        textRequest.setStream(true);
        return HttpClientUtil.sendStream(TEXT_COMPLETION_URL, JSONObject.toJSONString(textRequest),
                header, socketServer);
    }

    @Override
    public ImageResponse createImageGeneration(ImageRequest imageRequest) {
        ImageResponse response;
        try {
            JSONObject jsonObject = HttpClientUtil.sendNormally(IMAGE_GENERATE_URL, JSONObject.toJSONString(imageRequest), header);
            response = JSONObject.parseObject(jsonObject.toString(), ImageResponse.class);
        } catch (Throwable t) {
            log.error("createCompletion failed, data: {}, t: ",
                    JSONObject.toJSONString(imageRequest), t);
            throw new RuntimeException(t.getCause());
        }
        return response;
    }

    @Override
    public ChatResponse createChatCompletion(ChatRequest request) {
        JSONObject jsonObject = HttpClientUtil.sendNormally(CHAT_COMPLETION_URL, JSONObject.toJSONString(request), header);
        return JSONObject.parseObject(jsonObject.toString(), ChatResponse.class);
    }

    @Override
    public SseEmitter createChatCompletionStream(ChatRequest request) {
       return this.createChatCompletionStream(request, socketServer);
    }

    @Override
    public SseEmitter createChatCompletionStream(ChatRequest request, SocketServer socketServer) {
        request.setStream(true);
        return HttpClientUtil.sendStream(CHAT_COMPLETION_URL, JSONObject.toJSONString(request),
                header, socketServer);
    }
}
