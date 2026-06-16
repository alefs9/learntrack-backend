package com.upc.learntrack.course.controller;

import com.upc.learntrack.course.dto.GroupStatisticDto;
import com.upc.learntrack.course.dto.LearningCollectionDto;
import com.upc.learntrack.course.dto.TopicStatisticDto;
import com.upc.learntrack.course.service.LearningCollectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
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
    @PreAuthorize("hasAuthority('DOCENTE')")
    public ResponseEntity<List<LearningCollectionDto>> findMyCollections(Principal principal) {
        return ResponseEntity.ok(learningCollectionService.findAllMyCollections(principal.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LearningCollectionDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(learningCollectionService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('DOCENTE')")
    public ResponseEntity<LearningCollectionDto> save(@Valid @RequestBody LearningCollectionDto dto, Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(learningCollectionService.save(dto, principal.getName()));
    }

    // Estadísticas por colección con filtro de fechas
    @GetMapping("/{collectionName}/statistics")
    @PreAuthorize("hasAuthority('DOCENTE')")
    public ResponseEntity<List<TopicStatisticDto>> getCollectionStatistics(
            @PathVariable String collectionName,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        return ResponseEntity.ok(learningCollectionService.getStatisticsByCollection(collectionName, startDate, endDate));
    }

    // Comparativa entre grupos con filtro de fechas
    @GetMapping("/{collectionName}/groups-statistics")
    @PreAuthorize("hasAuthority('DOCENTE')")
    public ResponseEntity<List<GroupStatisticDto>> getGroupsStatistics(
            @PathVariable String collectionName,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        return ResponseEntity.ok(learningCollectionService.getGroupsStatistics(collectionName, startDate, endDate));
    }
}