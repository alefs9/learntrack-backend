package com.upc.learntrack.course.mapper;

import com.upc.learntrack.course.dto.EnrollmentDto;
import com.upc.learntrack.course.model.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mapping(source = "student.user.email", target = "studentEmail")
    @Mapping(source = "group.id", target = "groupId")
    EnrollmentDto toDto(Enrollment enrollment);

    @Mapping(target = "student", ignore = true)
    @Mapping(source = "groupId", target = "group.id")
    Enrollment toEntity(EnrollmentDto enrollmentDto);
}