package com.example.rbac.controller;

import com.example.rbac.service.LogStreamingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class LogStreamingController {

    @Autowired
    private LogStreamingService logStreamingService;

    @GetMapping(value = "/api/logs/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamLogs() {
        SseEmitter emitter = new SseEmitter(0L); // No timeout
        logStreamingService.streamProcessLogs(emitter);
        return emitter;
    }
    // When client sends message to /app/start-logs
    @MessageMapping("/start-logs")
    public void startLogs() {
        logStreamingService.startLogStreaming("session");
    }

}

