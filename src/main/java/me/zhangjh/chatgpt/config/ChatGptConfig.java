package me.zhangjh.chatgpt.config;

import me.zhangjh.chatgpt.client.ChatGptService;
import me.zhangjh.chatgpt.service.ChatGptServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangjh
 * @date 4:09 PM 2022/12/15
 * @Description
 */
@Configuration
public class ChatGptConfig {

    @Bean
    @ConditionalOnMissingBean
    public ChatGptService chatGptService() {
        return new ChatGptServiceImpl();
    }
}
