package me.zhangjh.chatgpt.socket;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.zhangjh.chatgpt.dto.response.ChatResponse;
import me.zhangjh.chatgpt.dto.response.ChatRet;
import me.zhangjh.chatgpt.dto.response.ChatStreamRet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import java.io.EOFException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author zhangjh451@midea.com
 * @date 3:03 PM 2023/3/13
 * @Description configure me as spring bean，you can override me to custom your sendMessage method
 */
@Data
@Slf4j
@Component
public class SocketServer {

    private String userId;
    private Session session;

    protected static ConcurrentMap<String, SocketServer> userMap = new ConcurrentHashMap<>();

    private static final String FINISH_FLAG = "[DONE]";

    @PostConstruct
    public void init() {
        log.info("chatSocketServer inited");
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        log.info("onOpen...userId: {}", userId);
        userMap.putIfAbsent(userId, this);
    }

    @OnClose
    public void onClose() {
        log.info("onClose..");
        userMap.remove(this.userId);
    }

    @OnMessage
    @SneakyThrows
    public void onMessage(String msg, Session session) {
        // in this project, client will not send message to server
    }

    /** you must override this method to implements you biz log
     *  message removed the beginning 'data:'
     * */
    public String sendMessage(String userId, String message) {
        log.info("userId: {}, message: {}", userId, message);
        if(StringUtils.isNotEmpty(message)) {
            Assert.isTrue(userMap.size() != 0, "socket not connected");
            Assert.isTrue(StringUtils.isNotEmpty(userId), "userId empty");

            if(FINISH_FLAG.equals(message)) {
                sendMsgInternal(userId, message);
                return message;
            }
            log.info("message: {}", message);
            ChatResponse chatResponse = JSONObject.parseObject(message, ChatResponse.class);
            List<ChatRet> choices = chatResponse.getChoices();
            StringBuilder partialMsg = new StringBuilder();
            for (ChatRet choice : choices) {
                List<ChatStreamRet> delta = choice.getDelta();
                for (ChatStreamRet ret : delta) {
                    if(ret != null && StringUtils.isNotEmpty(ret.getContent())) {
                        log.info("sendMsg, userId: {}, msg: {}", userId,
                                ret.getContent());
                        sendMsgInternal(userId, ret.getContent());
                        partialMsg.append(ret.getContent());
                    }
                }
            }
            return partialMsg.toString();
        }
        return "";
    }

    private void sendMsgInternal(String userId, String message) {
        try {
            SocketServer server = userMap.get(userId);
            Assert.isTrue(server != null, "socketServer empty");
            Session session = server.getSession();
            Assert.isTrue(session.isOpen(), "socket not connected");
            TextMessage textMessage = new TextMessage();
            textMessage.setUserId(userId);
            textMessage.setMessage(message);
            log.info("textMessage: {}", textMessage);
            synchronized (session.getId()) {
                session.getBasicRemote().sendText(JSONObject.toJSONString(textMessage));
            }
        } catch (IOException e) {
            log.error("socket sendText exception:", e);
        }
    }

    @OnError
    public void onError(Session session, Throwable t) {
        if(t instanceof EOFException) {
            log.info("socket timeout");
            this.onClose();
        }
    }

    @Data
    static class TextMessage {
        private String userId;
        private String message;
    }
}
