package me.zhangjh.chatgpt.dto.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;

/**
 * @author zhangjh
 * @date 3:05 PM 2022/12/15
 * @Description
 */
@Data
public class TextRequest {

    /** ID of the model to use */
    @NonNull
    private String model = "text-davinci-003";

    /** the propmts to generate completions for */
    private String prompt;

    /** Control the Creativity, it can be 0~1.
     * It’s not recommended to use the temperature with the Top_p parameter
     * */
    private double temperature;

    /** the suffix that comes after a completion of inserted text */
    private String suffix;

    /** the maximum num of tokens to generate in the completion */
    @JSONField(name = "max_tokens")
    private int maxTokens = 100;

    @JSONField(name = "top_p")
    private int topP = 1;

    /**
     * how many completions to generate for each prompt
     * default to 1
     * */
    private int n = 1;

    @JSONField(name = "frequency_penalty")
    private double frequencyPenalty = 0.0;

    @JSONField(name = "presence_penalty")

    private double presencePenalty = 0.0;

    private List<String> stop = Collections.singletonList("\n");
}
