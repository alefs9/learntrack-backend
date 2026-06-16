package com.upc.learntrack.learningpath.mapper;

import com.upc.learntrack.course.model.LearningCollection;
import com.upc.learntrack.learningpath.dto.ConceptualGapDto;
import com.upc.learntrack.learningpath.dto.LearningPathDto;
import com.upc.learntrack.learningpath.dto.PathNodeDto;
import com.upc.learntrack.learningpath.model.ConceptualGap;
import com.upc.learntrack.learningpath.model.LearningPath;
import com.upc.learntrack.learningpath.model.PathNode;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-15T21:41:15-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class LearningPathMapperImpl implements LearningPathMapper {

    @Autowired
    private PathNodeMapper pathNodeMapper;
    @Autowired
    private ConceptualGapMapper conceptualGapMapper;

    @Override
    public LearningPathDto toDto(LearningPath entity) {
        if ( entity == null ) {
            return null;
        }

        LearningPathDto learningPathDto = new LearningPathDto();

        learningPathDto.setCollectionName( entityLearningCollectionName( entity ) );
        learningPathDto.setConceptualGaps( conceptualGapListToConceptualGapDtoList( entity.getConceptualGaps() ) );
        learningPathDto.setCurrentPercentage( entity.getCurrentPercentage() );
        learningPathDto.setId( entity.getId() );
        learningPathDto.setPathNodes( pathNodeListToPathNodeDtoList( entity.getPathNodes() ) );
        learningPathDto.setStatus( entity.getStatus() );
        learningPathDto.setTargetPercentage( entity.getTargetPercentage() );
        learningPathDto.setUpdatedAt( entity.getUpdatedAt() );

        return learningPathDto;
    }

    private String entityLearningCollectionName(LearningPath learningPath) {
        if ( learningPath == null ) {
            return null;
        }
        LearningCollection learningCollection = learningPath.getLearningCollection();
        if ( learningCollection == null ) {
            return null;
        }
        String name = learningCollection.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    protected List<ConceptualGapDto> conceptualGapListToConceptualGapDtoList(List<ConceptualGap> list) {
        if ( list == null ) {
            return null;
        }

        List<ConceptualGapDto> list1 = new ArrayList<ConceptualGapDto>( list.size() );
        for ( ConceptualGap conceptualGap : list ) {
            list1.add( conceptualGapMapper.toDto( conceptualGap ) );
        }

        return list1;
    }

    protected List<PathNodeDto> pathNodeListToPathNodeDtoList(List<PathNode> list) {
        if ( list == null ) {
            return null;
        }

        List<PathNodeDto> list1 = new ArrayList<PathNodeDto>( list.size() );
        for ( PathNode pathNode : list ) {
            list1.add( pathNodeMapper.toDto( pathNode ) );
        }

        return list1;
    }
}
