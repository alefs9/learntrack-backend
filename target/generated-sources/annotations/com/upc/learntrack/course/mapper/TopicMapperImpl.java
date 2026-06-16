package com.upc.learntrack.course.mapper;

import com.upc.learntrack.course.dto.TopicDto;
import com.upc.learntrack.course.model.Topic;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-15T21:41:15-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class TopicMapperImpl implements TopicMapper {

    @Override
    public TopicDto toDto(Topic topic) {
        if ( topic == null ) {
            return null;
        }

        TopicDto topicDto = new TopicDto();

        topicDto.setId( topic.getId() );
        topicDto.setName( topic.getName() );
        topicDto.setOrderIdx( topic.getOrderIdx() );

        return topicDto;
    }

    @Override
    public Topic toEntity(TopicDto topicDto) {
        if ( topicDto == null ) {
            return null;
        }

        Topic topic = new Topic();

        topic.setName( topicDto.getName() );
        topic.setOrderIdx( topicDto.getOrderIdx() );

        return topic;
    }
}
