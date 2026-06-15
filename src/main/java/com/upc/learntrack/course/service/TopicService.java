package com.upc.learntrack.course.service;

import com.upc.learntrack.course.dto.TopicDto;
import java.util.List;

public interface TopicService {
    List<TopicDto> findAllByCollectionName(String collectionName);
    TopicDto findById(Long id);
    TopicDto save(String collectionName, TopicDto dto);
    List<TopicDto> findAll();
    List<TopicDto> findPrioritizedTopicsForStudent(String studentEmail);
}