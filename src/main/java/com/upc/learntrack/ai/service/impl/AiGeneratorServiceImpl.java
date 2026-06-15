package com.upc.learntrack.ai.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upc.learntrack.activity.dto.CreateFlashcardDto;
import com.upc.learntrack.activity.dto.CreateFlashcardSetDto;
import com.upc.learntrack.activity.dto.FlashcardSetDto;
import com.upc.learntrack.activity.dto.LearningActivityDto;
import com.upc.learntrack.activity.service.FlashcardService;
import com.upc.learntrack.activity.service.LearningActivityService;
import com.upc.learntrack.ai.dto.GenerateActivityResponseDto;
import com.upc.learntrack.ai.exception.AiGenerationException;
import com.upc.learntrack.ai.service.AiGeneratorService;
import com.upc.learntrack.ai.service.PromptBuilderService;
import com.upc.learntrack.course.exception.TopicNotFoundException;
import com.upc.learntrack.course.model.Topic;
import com.upc.learntrack.course.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiGeneratorServiceImpl implements AiGeneratorService {

    private final LearningActivityService learningActivityService;
    private final FlashcardService flashcardService;
    private final TopicRepository topicRepository;
    private final ObjectMapper objectMapper;
    private final RestClient restClient; // Bean configurado en RestClientConfig
    private final PromptBuilderService promptBuilder;

    @Override
    @Transactional
    public GenerateActivityResponseDto generateActivity(String topicName, String content, String userEmail, List<String> types) {
        try {
            Topic topic = topicRepository.findByName(topicName)
                    .orElseThrow(() -> new TopicNotFoundException("Tema no encontrado: " + topicName));

            String prompt = promptBuilder.buildMultiFormatPrompt(topicName, content, types);
            String response = callOllama(prompt);
            String generatedText = extractGeneratedText(response);

            JsonNode root = objectMapper.readTree(generatedText);
            GenerateActivityResponseDto result = new GenerateActivityResponseDto();

            if (types.contains("QUIZ")) {
                LearningActivityDto quiz = objectMapper.treeToValue(root.get("quiz"), LearningActivityDto.class);
                quiz.setGeneratedByAi(true);
                quiz.setStatus("DRAFT");
                LearningActivityDto savedQuiz = learningActivityService.save(topic.getId(), quiz, userEmail);
                result.setQuiz(savedQuiz);
            }

            if (types.contains("FLASHCARD")) {
                List<FlashcardSetDto> flashcardSets = objectMapper.readValue(
                        root.get("flashcards").traverse(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, FlashcardSetDto.class));

                for (FlashcardSetDto setDto : flashcardSets) {
                    CreateFlashcardSetDto createDto = new CreateFlashcardSetDto();
                    createDto.setTitle(setDto.getTitle());
                    if (setDto.getFlashcards() != null) {
                        List<CreateFlashcardDto> cards = setDto.getFlashcards().stream()
                                .map(f -> {
                                    CreateFlashcardDto card = new CreateFlashcardDto();
                                    card.setFront(f.getFront());
                                    card.setBack(f.getBack());
                                    return card;
                                }).toList();
                        createDto.setFlashcards(cards);
                    }
                    flashcardService.createFlashcardSet(topic.getId(), createDto, userEmail);
                }
                result.setFlashcards(flashcardSets);
            }

            return result;

        } catch (Exception e) {
            log.error("Error al generar actividad con IA", e);
            throw new AiGenerationException("Error al generar la actividad con IA local: " + e.getMessage(), e);
        }
    }

    private String callOllama(String prompt) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("model", "llama3");
        requestMap.put("prompt", prompt);
        requestMap.put("stream", false);
        requestMap.put("format", "json");

        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(requestMap);
        } catch (JsonProcessingException e) {
            throw new AiGenerationException("Error al serializar la solicitud a Ollama", e);
        }

        return restClient.post()
                .uri("/api/generate")
                .header("Content-Type", "application/json")
                .body(requestBody)
                .retrieve()
                .body(String.class);
    }

    private String extractGeneratedText(String ollamaResponse) throws Exception {
        return objectMapper.readTree(ollamaResponse).path("response").asText();
    }
}