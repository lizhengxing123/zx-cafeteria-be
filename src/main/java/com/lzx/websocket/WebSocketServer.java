package com.lzx.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket 服务器
 */
@Slf4j
@Component
@ServerEndpoint("/ws/{sid}")
public class WebSocketServer {

    // 存放会话对象
    private static final Map<String, Session> SESSIONS = new HashMap<>();

    /**
     * 连接建立成功调用的方法
     *
     * @param session 会话对象
     * @param sid     客户端标识
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        log.info("客户端连接建立成功, sid: {}", sid);
        SESSIONS.put(sid, session);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端消息
     * @param sid     客户端标识
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        log.info("收到客户端消息, sid: {}, message: {}", sid, message);
    }

    /**
     * 连接关闭调用的方法
     *
     * @param sid 客户端标识
     */
    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        log.info("客户端连接关闭, sid: {}", sid);
        SESSIONS.remove(sid);
    }

    /**
     * 发送消息给指定客户端
     *
     * @param sid     客户端标识
     * @param message 消息内容
     */
    public void sendMessage(String sid, String message) {
        Session session = SESSIONS.get(sid);
        if (session != null) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                log.error("发送消息给客户端失败, sid: {}, message: {}", sid, message, e);
            }
        }
    }

    /**
     * 发送消息给所有客户端
     *
     * @param message 消息内容
     */
    public void sendMessageToAll(String message) {
        for (Session session : SESSIONS.values()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                log.error("发送消息给客户端失败, message: {}", message, e);
            }
        }
    }
}
