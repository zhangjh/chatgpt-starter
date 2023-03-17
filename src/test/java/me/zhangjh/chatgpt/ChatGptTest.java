package me.zhangjh.chatgpt;

import me.zhangjh.chatgpt.client.ChatGptService;
import me.zhangjh.chatgpt.constant.RoleEnum;
import me.zhangjh.chatgpt.dto.Message;
import me.zhangjh.chatgpt.dto.request.ChatRequest;
import me.zhangjh.chatgpt.dto.request.ImageRequest;
import me.zhangjh.chatgpt.dto.request.TextRequest;
import me.zhangjh.chatgpt.dto.request.TranscriptionRequest;
import me.zhangjh.chatgpt.dto.response.ChatResponse;
import me.zhangjh.chatgpt.dto.response.ImageResponse;
import me.zhangjh.chatgpt.dto.response.TextResponse;
import me.zhangjh.chatgpt.dto.response.TranscriptionResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
//        textRequest.setPrompt("Q:将括号里的词汇翻译一下，如果是中文翻译成英文，如果是英文翻译成中文.（一只小狐狸正在吃葡萄）A:");
//        TextResponse textCompletion = chatGptService.createTextCompletion(textRequest);
        TextResponse textCompletion = chatGptService.createTextCompletion(textRequest, null);

        System.out.println(textCompletion);

    }

    @Test
    public void imageGenerateTest() {
        ImageRequest imageRequest = new ImageRequest();
        imageRequest.setPrompt("一只小狐狸正在吃葡萄");
        ImageResponse imageGeneration = chatGptService.createImageGeneration(imageRequest, null);
        System.out.println(imageGeneration);
    }

    @Test
    public void chatTest() {
        ChatRequest chatRequest = new ChatRequest();
        List<Message> messages = new ArrayList<>();
        Message message = new Message();
        message.setRole(RoleEnum.user.name());
        message.setContent("什么是斐波那契数列？");
        messages.add(message);
        Message answer = new Message();
        answer.setRole(RoleEnum.assistant.name());
        answer.setContent("斐波那契数列是指：1，1，2，3，5，8，13，21，34……这样一个由数列中前两项相加得出第三项的数列。这个数列与自然界中很多事物的增长规律有关，比如植物的叶片数量、兔子的繁殖规律等等。斐波那契数列的本质是递归定义，是算法和数学中的重要概念之一，也是计算机程序设计中常用的算法之一。");
        messages.add(answer);
        Message curMessage = new Message();
        curMessage.setRole(RoleEnum.user.name());
        curMessage.setContent("可以给出代码示例吗，java的？");
        messages.add(curMessage);
        chatRequest.setMessages(messages);
        Map<String, String> bizParams = new HashMap<>(1);
        bizParams.put("userId", "1");
        ChatResponse chatCompletion = chatGptService.createChatCompletion(chatRequest, bizParams);
        System.out.println(chatCompletion);
    }

    @Test
    public void audioTest() {
        TranscriptionRequest request = new TranscriptionRequest();
        request.setFile("/root/test.m4a");
        TranscriptionResponse transcription = chatGptService.createTranscription(request, null);
        System.out.println(transcription);
    }
}
