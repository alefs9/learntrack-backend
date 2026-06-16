package com.upc.learntrack.course.controller;

import com.upc.learntrack.course.dto.TopicDto;
import com.upc.learntrack.course.service.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @GetMapping("/collections/{collectionName}/topics")
    public ResponseEntity<List<TopicDto>> findAllByCollection(@PathVariable String collectionName,
                                                               Principal principal) {
        return ResponseEntity.ok(topicService.findAllByCollectionName(collectionName, principal != null ? principal.getName() : null));
    }

    @GetMapping("/topics/{id}")
    public ResponseEntity<TopicDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(topicService.findById(id));
    }

    @PostMapping("/collections/{collectionName}/topics")
    @PreAuthorize("hasAuthority('DOCENTE')")
    public ResponseEntity<TopicDto> save(@PathVariable String collectionName,
                                         @Valid @RequestBody TopicDto dto,
                                         Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(topicService.save(collectionName, dto, principal.getName()));
    }

    @GetMapping("/topics")
    @PreAuthorize("hasAnyAuthority('ESTUDIANTE', 'DOCENTE')")
    public ResponseEntity<List<TopicDto>> getAllTopics() {
        return ResponseEntity.ok(topicService.findAll());
    }

    @GetMapping("/topics/priorities")
    @PreAuthorize("hasAuthority('ESTUDIANTE')")
    public ResponseEntity<List<TopicDto>> getPrioritizedTopics(Principal principal) {
        return ResponseEntity.ok(topicService.findPrioritizedTopicsForStudent(principal.getName()));
    }
}