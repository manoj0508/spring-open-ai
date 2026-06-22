package com.manoj.spring.ai.app.config;

import com.manoj.spring.ai.app.advisor.TokenUsageAdvisors;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.openai.OpenAiChatOptions;
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
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder){
        //var options = OpenAiChatOptions.builder().model("gpt-5-nano").temperature(1.0).build();

         return chatClientBuilder.defaultAdvisors(List.of(new SimpleLoggerAdvisor(),
                            new TokenUsageAdvisors()))
                // .defaultOptions(options)
                .defaultSystem(systemPromptTemplate)
                .defaultUser("How can you help me ?")
                .build();
    }
}
