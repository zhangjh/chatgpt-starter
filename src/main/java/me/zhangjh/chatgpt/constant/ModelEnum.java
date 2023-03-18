package me.zhangjh.chatgpt.constant;

import lombok.Getter;

/**
 * @author njhxzhangjihong@126.com
 * @date 9:18 AM 2023/3/2
 * @Description
 */
@Getter
public enum ModelEnum {

    DAVINCI("text-davinci-003"),
    CHATGPT_TURBO("gpt-3.5-turbo"),
    CHATGPT_0301("gpt-3.5-turbo-0301"),
    ;

    private String code;

    ModelEnum(String code) {
        this.code = code;
    }
}
