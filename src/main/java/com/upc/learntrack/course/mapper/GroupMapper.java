package com.upc.learntrack.course.mapper;

import com.upc.learntrack.course.dto.GroupDto;
import com.upc.learntrack.course.model.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupMapper {
   GroupDto toDto(Group group);

   @Mapping(target = "id", ignore = true)
   @Mapping(target = "createdAt", ignore = true)
   @Mapping(target = "enrollments", ignore = true)
   @Mapping(target = "collectionGroups", ignore = true)
   Group toEntity(GroupDto groupDto);
}