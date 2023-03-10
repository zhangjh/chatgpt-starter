package me.zhangjh.chatgpt.dto.response;

import lombok.Data;
import me.zhangjh.chatgpt.dto.Message;

import java.util.List;

/**
 * @author zhangjh451@midea.com
 * @date 9:51 AM 2023/3/2
 * @Description
 */
@Data
public class ChatRet extends TextRet {

    private Message message;

    private List<ChatStreamRet> delta;
}
