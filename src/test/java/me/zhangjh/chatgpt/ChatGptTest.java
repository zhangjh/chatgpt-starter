package me.zhangjh.chatgpt;

import me.zhangjh.chatgpt.client.ChatGptService;
import me.zhangjh.chatgpt.dto.request.ImageRequest;
import me.zhangjh.chatgpt.dto.request.TextRequest;
import me.zhangjh.chatgpt.dto.response.ImageResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author zhangjh
 * @date 4:14 PM 2022/12/15
 * @Description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ChatGptTest {

    @Autowired
    private ChatGptService chatGptService;

    @Test
    public void textCompletionTest() {
        TextRequest textRequest = new TextRequest();
        textRequest.setPrompt("Q:写出java hello world？");
        textRequest.setTemperature(0.5);
        textRequest.setMaxTokens(2048);
        textRequest.setBestOf(1);
        textRequest.setTopP(1);
        textRequest.setStream(true);
//        textRequest.setPrompt("Q:将括号里的词汇翻译一下，如果是中文翻译成英文，如果是英文翻译成中文.（一只小狐狸正在吃葡萄）A:");
//        TextResponse textCompletion = chatGptService.createTextCompletion(textRequest);
        SseEmitter emitter = chatGptService.createTextCompletionStream(textRequest);

        emitter.onError(t -> System.out.println(t.getMessage()));

    }

    @Test
    public void imageGenerateTest() {
        ImageRequest imageRequest = new ImageRequest("一只小狐狸正在吃葡萄");
        ImageResponse imageGeneration = chatGptService.createImageGeneration(imageRequest);
        System.out.println(imageGeneration);
    }
}
