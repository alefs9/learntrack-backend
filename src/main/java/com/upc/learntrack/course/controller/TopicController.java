package com.upc.learntrack.course.controller;

import com.upc.learntrack.course.dto.TopicDto;
import com.upc.learntrack.course.service.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @GetMapping("/topics/collection/{collectionId}")
    public ResponseEntity<List<TopicDto>> findAllByCollection(@PathVariable Long collectionId) {
        return ResponseEntity.ok(topicService.findAllByCollection(collectionId));
    }

    @GetMapping("/topics/{id}")
    public ResponseEntity<TopicDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(topicService.findById(id));
    }

    @PostMapping("/collections/{collectionId}/topics")
    public ResponseEntity<TopicDto> save(@PathVariable Long collectionId, @Valid @RequestBody TopicDto dto) {
        dto.setLearningCollectionId(collectionId);
        return new ResponseEntity<>(topicService.save(dto), HttpStatus.CREATED);
    }
}