package com.manoj.spring.ai.app.config;

import com.manoj.spring.ai.app.advisor.TokenUsageAdvisors;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.beans.factory.annotation.Qualifier;
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
        return chatClientBuilder.defaultAdvisors(List.of(new SimpleLoggerAdvisor(),
                        new TokenUsageAdvisors()))
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
    public ChatClient messageChatClient(ChatClient.Builder chatClientBuilder,ChatMemory jdbcChatMemoryRepository) {
        Advisor loggerAdvisor = new SimpleLoggerAdvisor();
        Advisor tokenUsageAdvisor = new TokenUsageAdvisors();
        MessageChatMemoryAdvisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(jdbcChatMemoryRepository).build();
        return chatClientBuilder
                .defaultAdvisors(List.of(loggerAdvisor, messageChatMemoryAdvisor, tokenUsageAdvisor))
                .build();


    }
}
