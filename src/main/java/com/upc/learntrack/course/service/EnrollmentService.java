package com.upc.learntrack.course.service;

import com.upc.learntrack.course.dto.EnrollmentDto;
import java.util.List;

public interface EnrollmentService {
    List<EnrollmentDto> findAllByGroup(Long groupId);
    EnrollmentDto save(EnrollmentDto dto);
    void delete(Long id);
}