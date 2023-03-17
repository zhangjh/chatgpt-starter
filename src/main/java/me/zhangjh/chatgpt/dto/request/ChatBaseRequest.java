package me.zhangjh.chatgpt.dto.request;

import com.alibaba.fastjson.annotation.JSONField;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author zhangjh451@midea.com
 * @date 10:07 AM 2023/3/2
 * @Description
 */
@Data
public class ChatBaseRequest {

    /** ID of the model to use */
    @NotNull
    private String model = "text-davinci-003";

    /** Control the Creativity, it can be 0~1.
     * It’s not recommended to use the temperature with the Top_p parameter
     * */
    private Double temperature;

    /** the maximum num of tokens to generate in the completion */
    @JSONField(name = "max_tokens")
    private Integer maxTokens;

    @JSONField(name = "top_p")
    private Integer topP;

    /**
     * how many completions to generate for each prompt
     * default to 1
     * */
    private Integer n = 1;

    @JSONField(name = "frequency_penalty")
    private Double frequencyPenalty;

    @JSONField(name = "presence_penalty")

    private Double presencePenalty;

    /**
     * up to 4 sequences where the API stop generating more text.
     * */
    private String stop;

    /**
     * Whether to stream back partial progress.
     * */
    private Boolean stream = false;
}
