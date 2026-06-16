package com.upc.learntrack.course.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CollectionGroupId implements Serializable {

    @Column(name = "collection_id")
    private Long collectionId;

    @Column(name = "group_id")
    private Long groupId;
}