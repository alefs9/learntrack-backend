package com.upc.learntrack.course.service.impl;

import com.upc.learntrack.course.dto.TopicDto;
import com.upc.learntrack.course.exception.LearningCollectionNotFoundException;
import com.upc.learntrack.course.exception.TopicNotFoundException;
import com.upc.learntrack.course.mapper.TopicMapper;
import com.upc.learntrack.course.model.LearningCollection;
import com.upc.learntrack.course.model.Topic;
import com.upc.learntrack.course.repository.LearningCollectionRepository;
import com.upc.learntrack.course.repository.TopicRepository;
import com.upc.learntrack.course.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final LearningCollectionRepository learningCollectionRepository;
    private final TopicMapper topicMapper;

    @Override
    public List<TopicDto> findAllByCollection(Long collectionId) {
        return topicRepository.findAllByLearningCollectionIdOrderByOrderIdxAsc(collectionId).stream()
                .map(topicMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TopicDto findById(Long id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new TopicNotFoundException("Tema no encontrado con ID: " + id));
        return topicMapper.toDto(topic);
    }

    @Override
    public TopicDto save(TopicDto dto) {
        if (topicRepository.existsByNameAndLearningCollectionId(dto.getName(), dto.getLearningCollectionId())) {
            throw new IllegalArgumentException("Ya existe un tema con el nombre '" + dto.getName() + "' en esta colección.");
        }

        LearningCollection collection = learningCollectionRepository.findById(dto.getLearningCollectionId())
                .orElseThrow(() -> new LearningCollectionNotFoundException("Colección no encontrada con ID: " + dto.getLearningCollectionId()));

        Topic topic = topicMapper.toEntity(dto);
        topic.setLearningCollection(collection);
        return topicMapper.toDto(topicRepository.save(topic));
    }
}