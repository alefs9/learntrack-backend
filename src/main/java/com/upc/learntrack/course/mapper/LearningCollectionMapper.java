package com.upc.learntrack.course.mapper;

import com.upc.learntrack.course.dto.LearningCollectionDto;
import com.upc.learntrack.course.model.LearningCollection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LearningCollectionMapper {
    @Mapping(source = "teacher.id", target = "teacherId")
    LearningCollectionDto toDto(LearningCollection learningCollection);

    @Mapping(source = "teacherId", target = "teacher.id")
    LearningCollection toEntity(LearningCollectionDto learningCollectionDto);
}