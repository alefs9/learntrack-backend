package com.upc.learntrack.course.controller;

import com.upc.learntrack.course.dto.GroupDto;
import com.upc.learntrack.course.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    public ResponseEntity<List<GroupDto>> findAll() {
        return ResponseEntity.ok(groupService.findAll());
    }

    @GetMapping("/mine")
    public ResponseEntity<List<GroupDto>> findMyGroups(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // TODO: En el futuro extraerás el email con SecurityContextHolder
        return ResponseEntity.ok(groupService.findAll());
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<GroupDto>> findAllByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(groupService.findAllByTeacher(teacherId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.findById(id));
    }

    @PostMapping
    public ResponseEntity<GroupDto> save(@Valid @RequestBody GroupDto dto) {
        return new ResponseEntity<>(groupService.save(dto), HttpStatus.CREATED);
    }
}