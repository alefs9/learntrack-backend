package com.upc.learntrack.course.repository;

import com.upc.learntrack.course.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findAllByGroupId(Long groupId);

    boolean existsByStudentIdAndGroupId(Long studentId, Long groupId);
}