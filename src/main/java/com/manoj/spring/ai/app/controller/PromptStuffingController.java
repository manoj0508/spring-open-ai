package com.manoj.spring.ai.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PromptStuffingController {

    private static final Logger logger = LoggerFactory.getLogger(PromptStuffingController.class);

    private final ChatClient chatClient;

    public PromptStuffingController(ChatClient chatClient){
        this.chatClient = chatClient;
    }

    @GetMapping("/chat/prompt-stuffing")
    public String promptStuffing(@RequestParam("message") String message){
        logger.info("request request received for stuffing");
        return chatClient.prompt().user(message).call().content();
    }
}
