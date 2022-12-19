package me.zhangjh.chatgpt.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.zhangjh.chatgpt.client.ChatGptService;
import me.zhangjh.chatgpt.dto.request.ImageRequest;
import me.zhangjh.chatgpt.dto.request.TextRequest;
import me.zhangjh.chatgpt.dto.response.ImageResponse;
import me.zhangjh.chatgpt.dto.response.TextResponse;
import me.zhangjh.chatgpt.util.HttpClientUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.apache.commons.lang3.StringUtils;

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
    private String apiKey;

    private final Map<String, String> header = new HashMap<>();

    private static final String TEXT_COMPLETION_URL = "https://api.openai.com/v1/completions";

    private static final String IMAGE_GENERATE_URL = "https://api.openai.com/v1/images/generations";

    @PostConstruct
    public void init() {
        if(StringUtils.isEmpty(apiKey)) {
            apiKey = System.getenv("openai.apikey");
        }
        Assert.isTrue(StringUtils.isNotEmpty(apiKey), "openai apiKey not exist");
        header.put("Authorization", "Bearer " + apiKey);
    }

    @Override
    public TextResponse createTextCompletion(TextRequest textRequest) {
        TextResponse response = new TextResponse();
        try {
            JSONObject jsonObject = HttpClientUtil.sendHttp(TEXT_COMPLETION_URL, JSONObject.toJSONString(textRequest), header);
            response = JSONObject.parseObject(jsonObject.toString(), TextResponse.class);
        } catch (Throwable t) {
            log.error("createCompletion failed, data: {}, t: ",
                    JSONObject.toJSONString(textRequest), t);
        }
        return response;
    }

    @Override
    public ImageResponse createImageGeneration(ImageRequest imageRequest) {
        ImageResponse response = new ImageResponse();
        try {
            JSONObject jsonObject = HttpClientUtil.sendHttp(IMAGE_GENERATE_URL, JSONObject.toJSONString(imageRequest), header);
            response = JSONObject.parseObject(jsonObject.toString(), ImageResponse.class);
        } catch (Throwable t) {
            log.error("createCompletion failed, data: {}, t: ",
                    JSONObject.toJSONString(imageRequest), t);
        }
        return response;
    }
}
