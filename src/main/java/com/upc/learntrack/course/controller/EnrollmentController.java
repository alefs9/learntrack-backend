package com.upc.learntrack.course.controller;

import com.upc.learntrack.course.dto.EnrollmentDto;
import com.upc.learntrack.course.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<EnrollmentDto>> findAllByGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok(enrollmentService.findAllByGroup(groupId));
    }

    @PostMapping("/group/{groupId}")
    public ResponseEntity<EnrollmentDto> save(@PathVariable Long groupId, @Valid @RequestBody EnrollmentDto dto) {
        dto.setGroupId(groupId);
        return new ResponseEntity<>(enrollmentService.save(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        enrollmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}