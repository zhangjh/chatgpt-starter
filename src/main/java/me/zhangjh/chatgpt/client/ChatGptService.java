package me.zhangjh.chatgpt.client;

import me.zhangjh.chatgpt.dto.request.ChatRequest;
import me.zhangjh.chatgpt.dto.request.ImageRequest;
import me.zhangjh.chatgpt.dto.request.TextRequest;
import me.zhangjh.chatgpt.dto.request.TranscriptionRequest;
import me.zhangjh.chatgpt.dto.response.ChatResponse;
import me.zhangjh.chatgpt.dto.response.ImageResponse;
import me.zhangjh.chatgpt.dto.response.TextResponse;
import me.zhangjh.chatgpt.dto.response.TranscriptionResponse;
import me.zhangjh.chatgpt.socket.SocketServer;

import java.util.Map;
import java.util.function.Function;

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
    TextResponse createTextCompletion(TextRequest textRequest, Map<String, String> bizParams);

    /**
     * image generation
     * @param imageRequest
     * @return imageResponse
     */
    ImageResponse createImageGeneration(ImageRequest imageRequest, Map<String, String> bizParams);


    ChatResponse createChatCompletion(ChatRequest request, Map<String, String> bizParams);

    /**
     * for weixin only, transfer SseEmitter to WebSocket
     * */
    void createChatCompletionStream(ChatRequest request, Map<String, String> bizParams, SocketServer socketServer,
                                          Function<String, Void> bizCb);

    TranscriptionResponse createTranscription(TranscriptionRequest request, Map<String, String> bizParams);
}
