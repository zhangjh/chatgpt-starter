package me.zhangjh.chatgpt.dto.request;

import com.alibaba.fastjson.annotation.JSONField;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import me.zhangjh.chatgpt.constant.ModelEnum;

/**
 * @author zhangjh
 * @date 3:05 PM 2022/12/15
 * @Description
 */
@Data
public class TextRequest extends ChatBaseRequest {

    /** ID of the model to use */
    @NotNull
    private String model = ModelEnum.CHATGPT_TURBO.getCode();

    /** the propmts to generate completions for */
    @NotNull
    private String prompt;

    /** the suffix that comes after a completion of inserted text */
    private String suffix;

    /** the maximum num of tokens to generate in the completion */
    @JSONField(name = "max_tokens")
    private Integer maxTokens = 2048;

    @JSONField(name = "best_of")
    private Integer bestOf = 1;

    public void check() {
        if(this.getStream()) {
            throw new RuntimeException("do not support stream");
        }
    }
}
