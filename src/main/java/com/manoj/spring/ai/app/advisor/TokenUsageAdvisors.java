package com.manoj.spring.ai.app.advisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;


public class TokenUsageAdvisors implements CallAdvisor {

    private static final Logger logger = LoggerFactory.getLogger(TokenUsageAdvisors.class);

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
        ChatResponse chatResponse = chatClientResponse.chatResponse();
        ChatResponseMetadata metadata = chatResponse.getMetadata();
        if (null != metadata) {
            Usage usageDetails = metadata.getUsage();

            if (null != usageDetails) {
                logger.info("Token usage details {} ", usageDetails.toString());
            }

        }

        return chatClientResponse;
    }

    @Override
    public String getName() {
        return "TokenUsageAdvisors";
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
