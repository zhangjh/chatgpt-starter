package me.zhangjh.chatgpt.dto.response;

/**
 * @author njhxzhangjihong@126.com
 * @date 5:58 PM 2023/2/15
 * @Description
 */
public class BizException extends Exception {
    public BizException(String error) {
        super(error);
    }

    public BizException(Throwable cause) {
        super(cause);
    }
}
