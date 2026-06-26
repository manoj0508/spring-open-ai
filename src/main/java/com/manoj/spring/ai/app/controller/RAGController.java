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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rag/")
public class RAGController {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    @Value("classpath:/promptTemplates/systemPromptRandomDataTemplate.st")
    private Resource sentencePromptTemplate;

    @Value("classpath:/promptTemplates/hrPolicySystemPromptTemplate.st")
    Resource hrSystemTemplate;

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


    @GetMapping("/chat/hr-policy")
    public ResponseEntity<String> documentChat(@RequestHeader("username") String username,
                                               @RequestParam("message") String message) {
        SearchRequest searchRequest =
                SearchRequest.builder().query(message).topK(3).similarityThreshold(0.4).build();
        List<Document> similarDocs =  vectorStore.similaritySearch(searchRequest);
        String similarContext = similarDocs.stream()
                .map(Document::getText)
                .collect(Collectors.joining(System.lineSeparator()));
        String answer = chatClient.prompt()
                .system(promptSystemSpec -> promptSystemSpec.text(hrSystemTemplate)
                                .param("documents", similarContext))
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, username))
                .user(message)
                .call().content();
        return ResponseEntity.ok(answer);
    }
}
