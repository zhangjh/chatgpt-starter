package me.zhangjh.chatgpt.dto.request;

import lombok.Data;

/**
 * @author zhangjh451@midea.com
 * @date 6:01 PM 2023/3/14
 * @Description
 */
@Data
public class TranscriptionRequest {

    private String file;

    /**
     * An optional text to guide the model's style or continue a previous audio segment. The prompt should match the audio language.
     * */
    private String model = "whisper-1";

    private String prompt;

    private Integer temperature = 0;

    /**
     * The language of the input audio. Supplying the input language in ISO-639-1 format will improve accuracy and latency.
     * */
    private String language = "ISO-639-1";
}
