package com.manoj.spring.ai.app.config;

import com.manoj.spring.ai.app.advisor.TokenUsageAdvisors;
import org.springframework.ai.chat.client.ChatClientBuilderCustomizer;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientBuilderCustomizerConfig {


    @Bean
    public TokenUsageAdvisors chatTokenUsageAdvisor() {
        return new TokenUsageAdvisors();
    }

    @Bean
    public SimpleLoggerAdvisor chatLoggerAdvisor() {
        return new SimpleLoggerAdvisor();
    }

    @Bean
    public ChatClientBuilderCustomizer chatClientLoggingCustomizer(SimpleLoggerAdvisor chatLoggerAdvisor) {
        return build -> build.defaultAdvisors(chatLoggerAdvisor);
    }


    @Bean
    public ChatClientBuilderCustomizer chatClientTokenUsageCustomizer(TokenUsageAdvisors tokenUsageAdvisors) {
        return builder -> builder.defaultAdvisors(tokenUsageAdvisors);
    }

}
