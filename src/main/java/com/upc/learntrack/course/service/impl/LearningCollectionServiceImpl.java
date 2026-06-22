package com.upc.learntrack.course.service.impl;

import com.upc.learntrack.assessment.model.ActivityResult;
import com.upc.learntrack.assessment.repository.ActivityResultRepository;
import com.upc.learntrack.course.dto.GroupStatisticDto;
import com.upc.learntrack.course.dto.LearningCollectionDto;
import com.upc.learntrack.course.dto.TopicStatisticDto;
import com.upc.learntrack.course.exception.LearningCollectionNotFoundException;
import com.upc.learntrack.course.exception.TeacherNotFoundException;
import com.upc.learntrack.course.mapper.LearningCollectionMapper;
import com.upc.learntrack.course.model.*;
import com.upc.learntrack.course.repository.LearningCollectionRepository;
import com.upc.learntrack.course.repository.TeacherRepository;
import com.upc.learntrack.course.repository.TopicRepository;
import com.upc.learntrack.course.service.LearningCollectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LearningCollectionServiceImpl implements LearningCollectionService {

    private final LearningCollectionRepository learningCollectionRepository;
    private final TeacherRepository teacherRepository;
    private final LearningCollectionMapper learningCollectionMapper;
    private final TopicRepository topicRepository;
    private final ActivityResultRepository activityResultRepository;

    @Override
    @Transactional(readOnly = true)
    public List<LearningCollectionDto> findAll() {
        return learningCollectionRepository.findAll().stream()
                .map(learningCollectionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LearningCollectionDto> findAllMyCollections(String teacherEmail) {
        Teacher teacher = teacherRepository.findByUserEmail(teacherEmail)
                .orElseThrow(() -> new TeacherNotFoundException("Profesor no encontrado con correo: " + teacherEmail));
        return learningCollectionRepository.findAllByTeacherId(teacher.getId()).stream()
                .map(learningCollectionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public LearningCollectionDto findById(Long id) {
        LearningCollection collection = learningCollectionRepository.findById(id)
                .orElseThrow(() -> new LearningCollectionNotFoundException("Colección no encontrada con ID: " + id));
        return learningCollectionMapper.toDto(collection);
    }

    @Override
    @Transactional
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

    @Override
    @Transactional(readOnly = true)
    public List<TopicStatisticDto> getStatisticsByCollection(String collectionName, LocalDate startDate, LocalDate endDate) {
        LearningCollection collection = learningCollectionRepository.findByName(collectionName)
                .orElseThrow(() -> new LearningCollectionNotFoundException("Colección no encontrada"));
        List<Topic> topics = topicRepository.findAllByLearningCollectionId(collection.getId());
        List<TopicStatisticDto> result = new ArrayList<>();

        for (Topic topic : topics) {
            List<ActivityResult> results = activityResultRepository.findAll().stream()
                    .filter(r -> r.getActivity().getTopic().getId().equals(topic.getId()))
                    .filter(r -> startDate == null || !r.getCompletedAt().toLocalDate().isBefore(startDate))
                    .filter(r -> endDate == null || !r.getCompletedAt().toLocalDate().isAfter(endDate))
                    .toList();

            double avg = results.stream().mapToDouble(r -> r.getScore().doubleValue()).average().orElse(0.0);
            TopicStatisticDto dto = new TopicStatisticDto();
            dto.setTopicName(topic.getName());
            dto.setAverageMastery(BigDecimal.valueOf(avg));
            dto.setTotalSubmissions(results.size());
            result.add(dto);
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupStatisticDto> getGroupsStatistics(String collectionName, LocalDate startDate, LocalDate endDate) {
        LearningCollection collection = learningCollectionRepository.findByName(collectionName)
                .orElseThrow(() -> new LearningCollectionNotFoundException("Colección no encontrada"));
        List<Group> groups = collection.getCollectionGroups().stream()
                .map(CollectionGroup::getGroup)
                .toList();
        List<Topic> topics = topicRepository.findAllByLearningCollectionId(collection.getId());
        List<GroupStatisticDto> result = new ArrayList<>();

        for (Group group : groups) {
            GroupStatisticDto dto = new GroupStatisticDto();
            dto.setGroupCode(group.getCode());
            dto.setGroupName(group.getName());
            Map<String, BigDecimal> topicAvg = new HashMap<>();
            List<String> studentEmails = group.getStudentEmails();

            for (Topic topic : topics) {
                List<ActivityResult> results = activityResultRepository.findAll().stream()
                        .filter(r -> r.getActivity().getTopic().getId().equals(topic.getId()))
                        .filter(r -> studentEmails.contains(r.getStudent().getUser().getEmail()))
                        .filter(r -> startDate == null || !r.getCompletedAt().toLocalDate().isBefore(startDate))
                        .filter(r -> endDate == null || !r.getCompletedAt().toLocalDate().isAfter(endDate))
                        .toList();

                double avg = results.stream().mapToDouble(r -> r.getScore().doubleValue()).average().orElse(0.0);
                topicAvg.put(topic.getName(), BigDecimal.valueOf(avg));
            }
            dto.setTopicAverageMap(topicAvg);
            result.add(dto);
        }
        return result;
    }

    @Override
    @Transactional
    public LearningCollectionDto update(Long id, LearningCollectionDto dto, String teacherEmail) {
        LearningCollection collection = learningCollectionRepository.findById(id)
                .orElseThrow(() -> new LearningCollectionNotFoundException("Colección no encontrada"));

        if (!collection.getTeacher().getUser().getEmail().equals(teacherEmail)) {
            throw new SecurityException("No tienes permiso para modificar esta colección");
        }

        // Validar nombre duplicado (excepto sí mismo)
        if (!collection.getName().equals(dto.getName()) &&
                learningCollectionRepository.existsByNameAndTeacherId(dto.getName(), collection.getTeacher().getId())) {
            throw new IllegalArgumentException("Ya existe una colección con el nombre '" + dto.getName() + "'.");
        }

        collection.setName(dto.getName());
        collection.setDescription(dto.getDescription());
        return learningCollectionMapper.toDto(learningCollectionRepository.save(collection));
    }
}