package com.upc.learntrack.learningpath.mapper;

import com.upc.learntrack.course.model.Topic;
import com.upc.learntrack.learningpath.dto.PathNodeDto;
import com.upc.learntrack.learningpath.model.PathNode;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-15T21:41:15-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class PathNodeMapperImpl implements PathNodeMapper {

    @Override
    public PathNodeDto toDto(PathNode entity) {
        if ( entity == null ) {
            return null;
        }

        PathNodeDto pathNodeDto = new PathNodeDto();

        pathNodeDto.setTopicName( entityTopicName( entity ) );
        pathNodeDto.setCompleted( entity.getCompleted() );
        pathNodeDto.setId( entity.getId() );
        pathNodeDto.setMasteryScore( entity.getMasteryScore() );
        pathNodeDto.setOrderIdx( entity.getOrderIdx() );

        return pathNodeDto;
    }

    private String entityTopicName(PathNode pathNode) {
        if ( pathNode == null ) {
            return null;
        }
        Topic topic = pathNode.getTopic();
        if ( topic == null ) {
            return null;
        }
        String name = topic.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
