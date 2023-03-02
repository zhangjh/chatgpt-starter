package me.zhangjh.chatgpt.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author zhangjh451@midea.com
 * @date 9:50 AM 2023/3/2
 * @Description
 */
@Data
public class ChatResponse {

    private String id;

    private String object;

    private Date created;

    private String model;

    private String errorMsg;

    private List<ChatRet> choices;

    private CompletionUsage usage;

}
