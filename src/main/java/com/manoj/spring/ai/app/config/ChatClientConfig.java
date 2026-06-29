package com.manoj.spring.ai.app.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.List;

@Configuration
public class ChatClientConfig {

    @Value("classpath:promptTemplates/systemPromptTemplate.st")
    private Resource systemPromptTemplate;

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        //var options = OpenAiChatOptions.builder().model("gpt-5-nano").temperature(1.0).build();
        return chatClientBuilder
                // .defaultOptions(options)
                .defaultSystem(systemPromptTemplate)
                .defaultUser("How can you help me ?")
                .build();
    }

    @Bean(name = "jdbcH2ChatMemory")
    public ChatMemory jdbcH2ChatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .maxMessages(10)
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .build();
    }


    @Bean(name = "memoryChatClient")
    public ChatClient messageChatClient(ChatClient.Builder chatClientBuilder, ChatMemory jdbcChatMemoryRepository) {
        MessageChatMemoryAdvisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(jdbcChatMemoryRepository).build();
        return chatClientBuilder
                .defaultAdvisors(List.of(messageChatMemoryAdvisor))
                .build();


    }
}
