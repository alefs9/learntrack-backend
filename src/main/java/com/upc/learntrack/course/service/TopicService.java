package com.upc.learntrack.course.service;

import com.upc.learntrack.course.dto.TopicDto;
import java.util.List;

public interface TopicService {
    List<TopicDto> findAllByCollection(Long collectionId);
    TopicDto findById(Long id);
    TopicDto save(TopicDto dto);
}