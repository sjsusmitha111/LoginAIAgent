package com.example.aiaagent.controller;

import com.example.aiaagent.service.OpenAIService;
import com.example.aiaagent.service.OllamaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller to handle OpenAI requests.
 */
@RestController
@RequestMapping("/api/openai")
public class OpenAIController {

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private OllamaService ollamaService;

    @PostMapping("/chat")
    public String chat(@RequestBody String prompt) {
        return openAIService.getOpenAIResponse(prompt);
    }

    /**
     * Endpoint to chat with a local Ollama model.
     * POST /api/openai/ollama
     */
    @PostMapping("/ollama")
    public String ollamaChat(@RequestBody String prompt) {
        return ollamaService.getOllamaResponse(prompt);
    }
}