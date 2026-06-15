package com.upc.learntrack.course.service;

import com.upc.learntrack.course.dto.GroupDto;
import java.util.List;

public interface GroupService {
    List<GroupDto> findAll();
    List<GroupDto> findAllByTeacher(Long teacherId);
    GroupDto findById(Long id);
    GroupDto save(GroupDto dto);
}