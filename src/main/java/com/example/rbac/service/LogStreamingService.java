package com.example.rbac.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@Slf4j
public class LogStreamingService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void streamProcessLogs(SseEmitter emitter) {
        new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    String logLine = "Processing step " + i + " of 10...";
                    log.info(logLine);

                    emitter.send(SseEmitter.event()
                            .name("log")
                            .data(logLine));

                    Thread.sleep(1000); // Simulate time-consuming task
                }

                emitter.send(SseEmitter.event().name("complete").data("Process completed successfully!"));
                emitter.complete();
            } catch (Exception e) {
                log.error("Error during process", e);
                try {
                    emitter.send(SseEmitter.event().name("error").data("Error: " + e.getMessage()));
                } catch (Exception ignored) {}
                emitter.completeWithError(e);
            }
        }).start();
    }

    public void startLogStreaming(String sessionId) {
        new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    String logLine = "Processing step " + i + " at " + System.currentTimeMillis();
                    messagingTemplate.convertAndSend("/topic/logs", logLine);
                    Thread.sleep(1000); // simulate delay
                }
                messagingTemplate.convertAndSend("/topic/logs", "Log streaming completed!");
            } catch (InterruptedException e) {
                messagingTemplate.convertAndSend("/topic/logs", " Error: " + e.getMessage());
            }
        }).start();
    }
}

