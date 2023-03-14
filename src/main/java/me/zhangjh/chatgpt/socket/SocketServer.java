package me.zhangjh.chatgpt.socket;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.zhangjh.chatgpt.config.HttpSessionWSHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
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
 * @author zhangjh451@midea.com
 * @date 3:03 PM 2023/3/13
 * @Description configure me as spring bean，you can override me to custom your sendMessage method
 */
@Data
@ServerEndpoint(value = "/socket/chatStream/{userId}", configurator = HttpSessionWSHelper.class)
@Slf4j
@Component
public class SocketServer {

    private String userId;
    private Session session;

    private static ConcurrentMap<String, SocketServer> userMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        log.info("chatSocketServer inited");
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        log.info("onOpen...");
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
    }

    /** you must override this method to implements you biz log */
    public void sendMessage(String userId, String message, String bizContent) {
        if(StringUtils.isNotEmpty(message)) {
            Assert.isTrue(userMap.size() != 0, "socket not connected");
            Assert.isTrue(StringUtils.isNotEmpty(userId), "userId empty");
            SocketServer server = userMap.get(userId);
            Assert.isTrue(server != null && server.getSession().isOpen(), "socket not connected");
            try {
                server.getSession().getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.error("socket sendText exception:", e);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable t) {
        if(t instanceof EOFException) {
            log.info("socket timeout");
            this.onClose();
        }
    }
}
