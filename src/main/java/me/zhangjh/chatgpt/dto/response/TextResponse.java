package me.zhangjh.chatgpt.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author zhangjh
 * @date 3:02 PM 2022/12/15
 * @Description
 */
@Data
public class TextResponse {
    private String id;

    private String object;

    private Date created;

    private String model;

    private String errorMsg;

    private List<TextRet> choices;

    private CompletionUsage usage;
}
