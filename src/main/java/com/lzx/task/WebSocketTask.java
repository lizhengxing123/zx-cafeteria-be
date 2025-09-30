package com.lzx.task;

import com.lzx.websocket.WebSocketServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * WebSocket任务类
 */
@Slf4j
//@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebSocketTask {

    private final WebSocketServer webSocketServer;

    /**
     * 发送消息给所有客户端
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void sendMessageToAll() {
        log.info("发送 websocket 定时任务消息");
        webSocketServer.sendMessageToAll("websocket 定时任务消息");
    }
}
