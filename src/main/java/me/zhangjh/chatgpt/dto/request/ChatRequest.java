package me.zhangjh.chatgpt.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class ChatRequest extends ChatBaseRequest {

    @NotNull
    private String model = ModelEnum.CHATGPT_TURBO.getCode();

    @NotEmpty
    private List<Message> messages;
}

