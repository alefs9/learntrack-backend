package com.upc.learntrack.course.mapper;

import com.upc.learntrack.course.dto.TopicDto;
import com.upc.learntrack.course.model.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TopicMapper {
    @Mapping(source = "learningCollection.id", target = "learningCollectionId")
    TopicDto toDto(Topic topic);

    @Mapping(source = "learningCollectionId", target = "learningCollection.id")
    Topic toEntity(TopicDto topicDto);
}