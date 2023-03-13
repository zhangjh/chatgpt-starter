package me.zhangjh.chatgpt.dto.response;

import lombok.Data;

/**
 * @author zhangjh451@midea.com
 * @date 5:34 PM 2023/3/13
 * @Description
 */
@Data
public class ChatStreamRet extends TextRet {

    private String content;
}
