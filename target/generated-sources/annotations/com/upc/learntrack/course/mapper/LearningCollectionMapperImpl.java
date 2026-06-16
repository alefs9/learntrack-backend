package com.upc.learntrack.course.mapper;

import com.upc.learntrack.course.dto.LearningCollectionDto;
import com.upc.learntrack.course.model.LearningCollection;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-15T21:41:15-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class LearningCollectionMapperImpl implements LearningCollectionMapper {

    @Override
    public LearningCollectionDto toDto(LearningCollection learningCollection) {
        if ( learningCollection == null ) {
            return null;
        }

        LearningCollectionDto learningCollectionDto = new LearningCollectionDto();

        learningCollectionDto.setDescription( learningCollection.getDescription() );
        learningCollectionDto.setId( learningCollection.getId() );
        learningCollectionDto.setName( learningCollection.getName() );

        return learningCollectionDto;
    }

    @Override
    public LearningCollection toEntity(LearningCollectionDto learningCollectionDto) {
        if ( learningCollectionDto == null ) {
            return null;
        }

        LearningCollection learningCollection = new LearningCollection();

        learningCollection.setDescription( learningCollectionDto.getDescription() );
        learningCollection.setId( learningCollectionDto.getId() );
        learningCollection.setName( learningCollectionDto.getName() );

        return learningCollection;
    }
}
