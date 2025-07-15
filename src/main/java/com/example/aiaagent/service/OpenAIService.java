package com.example.aiaagent.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Service to interact with OpenAI API using the Chat Completions endpoint.
 */
@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String openAIApiKey;

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    public String getOpenAIResponse(String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAIApiKey);

        // Prepare messages as required by the chat API
        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        messages.add(userMessage);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 100);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(OPENAI_URL, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Object choices = response.getBody().get("choices");
            if (choices instanceof List && !((List<?>) choices).isEmpty()) {
                Object message = ((Map<?, ?>) ((List<?>) choices).get(0)).get("message");
                if (message instanceof Map) {
                    Object content = ((Map<?, ?>) message).get("content");
                    return content != null ? content.toString().trim() : "";
                }
            }
        }
        return "No response from OpenAI.";
    }
}