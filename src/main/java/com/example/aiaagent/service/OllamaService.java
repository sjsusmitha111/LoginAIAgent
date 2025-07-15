package com.example.aiaagent.service;

import com.example.aiaagent.model.Message;
import com.example.aiaagent.model.OllamaResponse;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Service to interact with a local Ollama server for LLM chat.
 */
@Service
public class OllamaService {

    // Ollama server endpoint (default)
    private static final String OLLAMA_URL = "http://localhost:11434/api/chat";

    /**
     * Sends a chat prompt to the Ollama server and returns the response.
     *
     * @param prompt The user's message.
     * @return The model's reply.
     */
    public String getOllamaResponse(String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Request body as per Ollama's API
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "phi3"); // Change model name as needed
        requestBody.put("messages", new Object[]{
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", prompt);
                }}
        });
        requestBody.put("stream", false);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);


        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(OLLAMA_URL, entity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(response.getBody());
                JsonNode root = objectMapper.readTree(json);
                return root.path("message").path("content").asText();
            } else {
                return "No response from Ollama.";
            }
            /*if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Object message = response.getBody().get("message");
                if (message instanceof Map) {
                    Object content = ((Map<?, ?>) message).get("content");
                    return content != null ? content.toString().trim() : "";
                }
                *//*String[] lines = response.getBody().split("\n");
                String lastLine = lines[lines.length - 1];
                ObjectMapper mapper = new ObjectMapper();
                Map<?, ?> json = mapper.readValue(lastLine, Map.class);*//*
                *//*Object message = json.get("message");
                if (message instanceof Map) {
                    Object content = ((Map<?, ?>) message).get("content");
                    return content != null ? content.toString().trim() : "";
                }*//*
            }*/

            /*if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
//                String[] lines = response.getBody();
//                String lastLine = lines[lines.length - 1];
                ObjectMapper mapper = new ObjectMapper();
                OllamaResponse ollamaResponse = mapper.readValue((JsonParser) response.getBody().get("HttpStatus.OK"), OllamaResponse.class);
                Message message = ollamaResponse.getMessage();
                return (message != null && message.getContent() != null) ? message.getContent().trim() : "";
            }*/

        } catch (Exception e) {
            return "Error communicating with Ollama: " + e.getMessage();
        }
    }
}