package com.manoj.spring.ai.app.controller;

import com.manoj.spring.ai.app.advisor.TokenUsageAdvisors;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rag/")
public class RAGController {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    @Value("classpath:/promptTemplates/systemPromptRandomDataTemplate.st")
    private Resource sentencePromptTemplate;

    public RAGController(@Qualifier("memoryChatClient") ChatClient chatClient, VectorStore vectorStore){
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }


    @GetMapping("/random/chat")
    public ResponseEntity<String> randonDataChat(@RequestHeader("username") String username,
                                                 @RequestParam String message){
        SearchRequest searchRequest = SearchRequest.builder().
                query(message)
                .topK(3)
                .similarityThreshold(0.3)
                .build();

        List<Document> similarDocumentList = vectorStore.similaritySearch(searchRequest);

        String llmResponse = chatClient.prompt()
                .system(promptSystemSpec -> promptSystemSpec.text(sentencePromptTemplate).param("documents", similarDocumentList))
                .user(message)
                .advisors(a-> a.param(ChatMemory.CONVERSATION_ID, username))
                .call()
                .content();

        return ResponseEntity.ok(llmResponse);

    }
}
