package com.upc.learntrack.course.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "topics", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"collection_id", "name"})
})
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id", nullable = false)
    private LearningCollection learningCollection;

    @Column(name = "order_idx", nullable = false)
    @Builder.Default
    private Integer orderIdx = 0;
}