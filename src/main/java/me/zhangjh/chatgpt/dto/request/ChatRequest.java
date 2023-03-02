package me.zhangjh.chatgpt.dto.request;

import lombok.Data;
import me.zhangjh.chatgpt.constant.ModelEnum;
import me.zhangjh.chatgpt.dto.Message;

import java.util.List;

/**
 * @author zhangjh451@midea.com
 * @date 9:29 AM 2023/3/2
 * @Description
 */
@Data
public class ChatRequest extends BaseRequest {

    private String model = ModelEnum.CHATGPT_TURBO.getCode();

    private List<Message> messages;
}

