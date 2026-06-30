package com.manoj.spring.ai.app.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mcp")
public class MCPClientController {

    private final ChatClient chatClient;

    public MCPClientController(@Qualifier("mcpClient1ChatClient") ChatClient chatClient){
        this.chatClient = chatClient;

    }



    @GetMapping("/chat")
    public String chat(@RequestHeader(value = "username",required = false) String username,
                       @RequestParam("message") String message) {
        return chatClient.prompt().user(message+ " My username is " + username)
                .call().content();
    }
}
