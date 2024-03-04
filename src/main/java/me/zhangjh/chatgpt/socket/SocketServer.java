package me.zhangjh.chatgpt.socket;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.EOFException;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author njhxzhangjihong@126.com
 * @date 3:03 PM 2023/3/13
 * @Description configure me as spring bean，you can override me to custom your sendMessage method
 */
@Data
@Slf4j
@ServerEndpoint("/socket/chatStream/{userId}")
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
        // should notice clients to update connect status
        // when multiple clients connected
        SocketServer server = userMap.get(this.userId);
        if(server != null) {
            try {
                server.getSession().close();
            } catch (IOException e) {
                log.error("socketServer close exception:", e);
            }
        }
        userMap.remove(this.userId);
    }

    @OnMessage
    public void onMessage(String msg, Session session) {
        // in this project, client will not send message to server
    }

    /** you must override this method to implements you biz log
     *  message removed the beginning 'data:'
     * */
    public void sendMessage(String userId, String message) {
        log.info("userId: {}, message: {}", userId, message);
        if(StringUtils.isNotEmpty(message)) {
            Assert.isTrue(userMap.size() != 0, "socket not connected");
            Assert.isTrue(StringUtils.isNotEmpty(userId), "userId empty");

            log.info("message: {}", message);
            sendMsgInternal(userId, message);
        }
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
