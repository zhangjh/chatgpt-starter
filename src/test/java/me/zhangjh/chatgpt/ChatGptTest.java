package me.zhangjh.chatgpt;

import me.zhangjh.chatgpt.client.ChatGptService;
import me.zhangjh.chatgpt.dto.request.ImageRequest;
import me.zhangjh.chatgpt.dto.request.TextRequest;
import me.zhangjh.chatgpt.dto.response.ImageResponse;
import me.zhangjh.chatgpt.dto.response.TextResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
        textRequest.setPrompt("Q:将括号里的词汇翻译一下，如果是中文翻译成英文，如果是英文翻译成中文.（一只小狐狸正在吃葡萄）A:");
        TextResponse textCompletion = chatGptService.createTextCompletion(textRequest);
        System.out.println(textCompletion);
    }

    @Test
    public void imageGenerateTest() {
        ImageRequest imageRequest = new ImageRequest("一只小狐狸正在吃葡萄");
        ImageResponse imageGeneration = chatGptService.createImageGeneration(imageRequest);
        System.out.println(imageGeneration);
    }
}
