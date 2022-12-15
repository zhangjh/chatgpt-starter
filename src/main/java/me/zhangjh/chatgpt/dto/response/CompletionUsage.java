package me.zhangjh.chatgpt.dto.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author zhangjh
 * @date 3:03 PM 2022/12/15
 * @Description
 */
@Data
public class CompletionUsage {
    @JSONField(name = "prompt_tokens")
    private int promptTokens;

    @JSONField(name = "completion_tokens")
    private int completionTokens;

    @JSONField(name = "total_tokens")
    private int totalTokens;
}
