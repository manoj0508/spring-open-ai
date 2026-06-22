package com.manoj.spring.ai.app.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * This api supports stream data response using Flux
 */

@RestController
@RequestMapping("/api")
public class StreamChatController {

    private final ChatClient chatClient;

    public StreamChatController(ChatClient chatClient){
        this.chatClient = chatClient;
    }


    @GetMapping("/chat/stream")
    public Flux<String> streamChat(@RequestParam("message") String message){
        return chatClient.prompt().user(message).stream().content();
    }

}
