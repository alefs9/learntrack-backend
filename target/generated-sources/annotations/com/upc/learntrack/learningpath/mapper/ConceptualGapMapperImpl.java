package com.upc.learntrack.learningpath.mapper;

import com.upc.learntrack.course.model.Topic;
import com.upc.learntrack.learningpath.dto.ConceptualGapDto;
import com.upc.learntrack.learningpath.model.ConceptualGap;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-15T21:41:15-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ConceptualGapMapperImpl implements ConceptualGapMapper {

    @Override
    public ConceptualGapDto toDto(ConceptualGap entity) {
        if ( entity == null ) {
            return null;
        }

        ConceptualGapDto conceptualGapDto = new ConceptualGapDto();

        conceptualGapDto.setTopicName( entityTopicName( entity ) );
        conceptualGapDto.setResolved( entity.isResolved() );
        conceptualGapDto.setDescription( entity.getDescription() );
        conceptualGapDto.setId( entity.getId() );

        return conceptualGapDto;
    }

    private String entityTopicName(ConceptualGap conceptualGap) {
        if ( conceptualGap == null ) {
            return null;
        }
        Topic topic = conceptualGap.getTopic();
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
