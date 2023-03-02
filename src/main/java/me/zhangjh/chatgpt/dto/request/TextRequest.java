package me.zhangjh.chatgpt.dto.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NonNull;
import me.zhangjh.chatgpt.constant.ModelEnum;

/**
 * @author zhangjh
 * @date 3:05 PM 2022/12/15
 * @Description
 */
@Data
public class TextRequest extends BaseRequest {

    /** ID of the model to use */
    @NonNull
    private String model = ModelEnum.DAVINCI.getCode();

    /** the propmts to generate completions for */
    private String prompt;

    /** the suffix that comes after a completion of inserted text */
    private String suffix;

    /** the maximum num of tokens to generate in the completion */
    @JSONField(name = "max_tokens")
    private Integer maxTokens = 2048;

    @JSONField(name = "best_of")
    private Integer bestOf = 1;
}
