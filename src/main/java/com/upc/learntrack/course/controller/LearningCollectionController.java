package com.upc.learntrack.course.controller;

import com.upc.learntrack.course.dto.LearningCollectionDto;
import com.upc.learntrack.course.service.LearningCollectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal; // Importante para leer el token
import java.util.List;

@RestController
@RequestMapping("/api/v1/collections")
@RequiredArgsConstructor
public class LearningCollectionController {

    private final LearningCollectionService learningCollectionService;

    @GetMapping
    public ResponseEntity<List<LearningCollectionDto>> findAll() {
        return ResponseEntity.ok(learningCollectionService.findAll());
    }

    @GetMapping("/mine")
    public ResponseEntity<List<LearningCollectionDto>> findMyCollections() {
        return ResponseEntity.ok(learningCollectionService.findAll());
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<LearningCollectionDto>> findAllByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(learningCollectionService.findAllByTeacher(teacherId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LearningCollectionDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(learningCollectionService.findById(id));
    }

    @PostMapping
    public ResponseEntity<LearningCollectionDto> save(@Valid @RequestBody LearningCollectionDto dto, Principal principal) {
        return new ResponseEntity<>(learningCollectionService.save(dto, principal.getName()), HttpStatus.CREATED);
    }
}