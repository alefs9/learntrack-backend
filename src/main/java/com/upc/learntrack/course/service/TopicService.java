package com.upc.learntrack.course.service;

import com.upc.learntrack.course.dto.TopicDto;
import java.util.List;

public interface TopicService {
    List<TopicDto> findAllByCollectionName(String collectionName, String teacherEmail);
    TopicDto findById(Long id);
    TopicDto save(String collectionName, TopicDto dto, String teacherEmail);
    List<TopicDto> findAll();
    List<TopicDto> findPrioritizedTopicsForStudent(String studentEmail);
}