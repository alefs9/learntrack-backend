package com.upc.learntrack.course.mapper;

import com.upc.learntrack.course.dto.CollectionGroupDto;
import com.upc.learntrack.course.model.CollectionGroup;
import com.upc.learntrack.course.model.Group;
import com.upc.learntrack.course.model.LearningCollection;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-15T21:41:15-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class CollectionGroupMapperImpl implements CollectionGroupMapper {

    @Override
    public CollectionGroupDto toDto(CollectionGroup collectionGroup) {
        if ( collectionGroup == null ) {
            return null;
        }

        CollectionGroupDto collectionGroupDto = new CollectionGroupDto();

        collectionGroupDto.setLearningCollectionId( collectionGroupLearningCollectionId( collectionGroup ) );
        collectionGroupDto.setGroupId( collectionGroupGroupId( collectionGroup ) );

        return collectionGroupDto;
    }

    @Override
    public CollectionGroup toEntity(CollectionGroupDto collectionGroupDto) {
        if ( collectionGroupDto == null ) {
            return null;
        }

        CollectionGroup.CollectionGroupBuilder collectionGroup = CollectionGroup.builder();

        collectionGroup.learningCollection( collectionGroupDtoToLearningCollection( collectionGroupDto ) );
        collectionGroup.group( collectionGroupDtoToGroup( collectionGroupDto ) );

        return collectionGroup.build();
    }

    private Long collectionGroupLearningCollectionId(CollectionGroup collectionGroup) {
        if ( collectionGroup == null ) {
            return null;
        }
        LearningCollection learningCollection = collectionGroup.getLearningCollection();
        if ( learningCollection == null ) {
            return null;
        }
        Long id = learningCollection.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long collectionGroupGroupId(CollectionGroup collectionGroup) {
        if ( collectionGroup == null ) {
            return null;
        }
        Group group = collectionGroup.getGroup();
        if ( group == null ) {
            return null;
        }
        Long id = group.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected LearningCollection collectionGroupDtoToLearningCollection(CollectionGroupDto collectionGroupDto) {
        if ( collectionGroupDto == null ) {
            return null;
        }

        LearningCollection learningCollection = new LearningCollection();

        learningCollection.setId( collectionGroupDto.getLearningCollectionId() );

        return learningCollection;
    }

    protected Group collectionGroupDtoToGroup(CollectionGroupDto collectionGroupDto) {
        if ( collectionGroupDto == null ) {
            return null;
        }

        Group group = new Group();

        group.setId( collectionGroupDto.getGroupId() );

        return group;
    }
}
