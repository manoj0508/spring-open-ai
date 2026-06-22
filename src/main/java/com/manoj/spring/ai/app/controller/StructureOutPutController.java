package com.manoj.spring.ai.app.controller;

import com.manoj.spring.ai.app.model.CountryCities;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/structure/")
public class StructureOutPutController {

    private final ChatClient chatClient;

    public StructureOutPutController(ChatClient.Builder chatBuilder) {
        this.chatClient = chatBuilder.defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }


    @GetMapping("/city")
    public ResponseEntity<CountryCities> cityWCountry(String message) {
        CountryCities cityDetails = chatClient.prompt().user(message).call().entity(CountryCities.class);
        return ResponseEntity.ok(cityDetails);
    }


    @GetMapping("/city-list")
    public ResponseEntity<List<String>> cityList(String message) {
        List<String> cityList = chatClient.prompt().user(message).call().entity(new ListOutputConverter());
        return ResponseEntity.ok(cityList);

    }

    @GetMapping("/city-bean-list")
    public ResponseEntity<List<CountryCities>> cityBeanList(String message) {
        List<CountryCities> cityBeanList = chatClient.prompt().
                user(message)
                .call()
                .entity(new ParameterizedTypeReference<List<CountryCities>>() {
                         });
        return ResponseEntity.ok(cityBeanList);

    }
}
