package com.upc.learntrack.course.service.impl;

import com.upc.learntrack.course.dto.LearningCollectionDto;
import com.upc.learntrack.course.exception.LearningCollectionNotFoundException;
import com.upc.learntrack.course.exception.TeacherNotFoundException;
import com.upc.learntrack.course.mapper.LearningCollectionMapper;
import com.upc.learntrack.course.model.LearningCollection;
import com.upc.learntrack.course.model.Teacher;
import com.upc.learntrack.course.repository.LearningCollectionRepository;
import com.upc.learntrack.course.repository.TeacherRepository;
import com.upc.learntrack.course.service.LearningCollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LearningCollectionServiceImpl implements LearningCollectionService {

    private final LearningCollectionRepository learningCollectionRepository;
    private final TeacherRepository teacherRepository;
    private final LearningCollectionMapper learningCollectionMapper;

    @Override
    public List<LearningCollectionDto> findAll() {
        return learningCollectionRepository.findAll().stream()
                .map(learningCollectionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LearningCollectionDto> findAllByTeacher(Long teacherId) {
        return learningCollectionRepository.findAllByTeacherId(teacherId).stream()
                .map(learningCollectionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public LearningCollectionDto findById(Long id) {
        LearningCollection collection = learningCollectionRepository.findById(id)
                .orElseThrow(() -> new LearningCollectionNotFoundException("Colección no encontrada con ID: " + id));
        return learningCollectionMapper.toDto(collection);
    }

    @Override
    public LearningCollectionDto save(LearningCollectionDto dto, String teacherEmail) {
        Teacher teacher = teacherRepository.findByUserEmail(teacherEmail)
                .orElseThrow(() -> new TeacherNotFoundException("Profesor no encontrado con correo: " + teacherEmail));

        if (learningCollectionRepository.existsByNameAndTeacherId(dto.getName(), teacher.getId())) {
            throw new IllegalArgumentException("Ya has creado una colección con el nombre '" + dto.getName() + "'.");
        }

        LearningCollection collection = learningCollectionMapper.toEntity(dto);
        collection.setTeacher(teacher);
        return learningCollectionMapper.toDto(learningCollectionRepository.save(collection));
    }
}