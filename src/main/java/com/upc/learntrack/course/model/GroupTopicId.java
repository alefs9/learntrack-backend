package com.upc.learntrack.course.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupTopicId implements Serializable {
    private Long groupId;
    private Long topicId;
}