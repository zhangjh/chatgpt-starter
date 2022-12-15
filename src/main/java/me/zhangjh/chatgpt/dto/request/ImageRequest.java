package me.zhangjh.chatgpt.dto.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NonNull;

/**
 * @author zhangjh
 * @date 2022/12/15
 * @Description
 */
@Data
public class ImageRequest {

    /** a text description of the desired images */
    @NonNull
    private String prompt;

    /** a num >=1 && <= 10, images to generate*/
    private int n = 1;

    /** the size of images */
    private String size = "1024x1024";

    /** must be url or b64_json, default url */
    @JSONField(name = "response_format")
    private String responseFormat = "url";
}
