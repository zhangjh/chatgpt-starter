package me.zhangjh.chatgpt.dto;

import lombok.Data;

/**
 * @author zhangjh451@midea.com
 * @date 9:53 AM 2023/3/2
 * @Description
 */
@Data
public class Message {
    private String role;
    private String content;
}
