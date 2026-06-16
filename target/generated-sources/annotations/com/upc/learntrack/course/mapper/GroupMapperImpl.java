package com.upc.learntrack.course.mapper;

import com.upc.learntrack.course.dto.GroupDto;
import com.upc.learntrack.course.model.Group;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-15T21:41:15-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class GroupMapperImpl implements GroupMapper {

    @Override
    public GroupDto toDto(Group group) {
        if ( group == null ) {
            return null;
        }

        GroupDto groupDto = new GroupDto();

        groupDto.setCode( group.getCode() );
        groupDto.setCreatedAt( group.getCreatedAt() );
        groupDto.setId( group.getId() );
        groupDto.setName( group.getName() );

        return groupDto;
    }

    @Override
    public Group toEntity(GroupDto groupDto) {
        if ( groupDto == null ) {
            return null;
        }

        Group group = new Group();

        group.setCode( groupDto.getCode() );
        group.setName( groupDto.getName() );

        return group;
    }
}
