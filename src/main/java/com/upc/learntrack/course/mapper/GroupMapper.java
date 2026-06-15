package com.upc.learntrack.course.mapper;

import com.upc.learntrack.course.dto.GroupDto;
import com.upc.learntrack.course.model.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    @Mapping(source = "teacher.id", target = "teacherId")
    GroupDto toDto(Group group);

    @Mapping(source = "teacherId", target = "teacher.id")
    Group toEntity(GroupDto groupDto);
}