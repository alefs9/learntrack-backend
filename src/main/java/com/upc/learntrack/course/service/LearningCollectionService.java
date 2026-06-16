package com.upc.learntrack.course.service;

import com.upc.learntrack.course.dto.LearningCollectionDto;

import java.util.List;

public interface LearningCollectionService {

    List<LearningCollectionDto> findAll();

    List<LearningCollectionDto> findAllByTeacher(Long teacherId);

    LearningCollectionDto findById(Long id);

    LearningCollectionDto save(LearningCollectionDto dto, String teacherEmail);
}