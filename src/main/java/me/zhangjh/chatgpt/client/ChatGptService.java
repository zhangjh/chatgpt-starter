package me.zhangjh.chatgpt.client;

import me.zhangjh.chatgpt.dto.request.ChatRequest;
import me.zhangjh.chatgpt.dto.request.ImageRequest;
import me.zhangjh.chatgpt.dto.request.TextRequest;
import me.zhangjh.chatgpt.dto.response.ChatResponse;
import me.zhangjh.chatgpt.dto.response.ImageResponse;
import me.zhangjh.chatgpt.dto.response.TextResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author zhangjh
 * @date 1:41 PM 2022/12/15
 * @Description
 */
public interface ChatGptService {

    /**
     * text completion
     * @param data
     * @return chatResponse
     */
    TextResponse createTextCompletion(TextRequest data);

    /**
     * if textRequest.stream set to true, plz use this interface
     * */
    SseEmitter createTextCompletionStream(TextRequest data);

    /**
     * image generation
     * @param imageRequest
     * @return imageResponse
     */
    ImageResponse createImageGeneration(ImageRequest imageRequest);


    ChatResponse createChatCompletion(ChatRequest request);
}
