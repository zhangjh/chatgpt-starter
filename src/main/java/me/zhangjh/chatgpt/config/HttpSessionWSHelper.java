package me.zhangjh.chatgpt.config;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * @author zhangjh451@midea.com
 * @date 3:33 PM 2023/3/13
 * @Description
 */
public class HttpSessionWSHelper extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession session = (HttpSession) request.getHttpSession();
        if (session != null){
            sec.getUserProperties().put(HttpSession.class.getName(), session);
        }
    }
}
