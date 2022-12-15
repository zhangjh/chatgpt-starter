package me.zhangjh.chatgpt.dto.response;

import lombok.Data;

/**
 * @author zhangjh
 * @date 3:02 PM 2022/12/15
 * @Description
 */
@Data
public class TextRet {
    private String text;

    private int index;

    private String finishReason;
}
