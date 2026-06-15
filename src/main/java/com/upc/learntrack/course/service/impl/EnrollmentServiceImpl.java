package com.upc.learntrack.course.service.impl;

import com.upc.learntrack.course.dto.EnrollmentDto;
import com.upc.learntrack.course.exception.EnrollmentNotFoundException;
import com.upc.learntrack.course.exception.GroupNotFoundException;
import com.upc.learntrack.course.exception.StudentNotFoundException;
import com.upc.learntrack.course.mapper.EnrollmentMapper;
import com.upc.learntrack.course.model.Enrollment;
import com.upc.learntrack.course.model.Group;
import com.upc.learntrack.course.model.Student;
import com.upc.learntrack.course.repository.EnrollmentRepository;
import com.upc.learntrack.course.repository.GroupRepository;
import com.upc.learntrack.course.repository.StudentRepository;
import com.upc.learntrack.course.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final EnrollmentMapper enrollmentMapper;

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentDto> findAllByGroup(Long groupId) {
        return enrollmentRepository.findAllByGroupId(groupId).stream()
                .map(enrollmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EnrollmentDto save(EnrollmentDto dto) {
        // 1. Buscamos al estudiante por correo
        Student student = studentRepository.findByUserEmail(dto.getStudentEmail())
                .orElseThrow(() -> new StudentNotFoundException("Estudiante no encontrado con el correo: " + dto.getStudentEmail()));

        // 2. Buscamos el grupo por su ID
        Group group = groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new GroupNotFoundException("Grupo no encontrado con ID: " + dto.getGroupId()));

        // 3. Validamos que el alumno no esté ya matriculado
        if (enrollmentRepository.existsByStudentIdAndGroupId(student.getId(), group.getId())) {
            throw new IllegalArgumentException("El estudiante ya se encuentra matriculado en este grupo.");
        }

        // 4. Guardamos la matrícula
        Enrollment enrollment = enrollmentMapper.toEntity(dto);
        enrollment.setStudent(student);
        enrollment.setGroup(group);
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        EnrollmentDto response = enrollmentMapper.toDto(savedEnrollment);
        response.setStudentEmail(student.getUser().getEmail());
        return response;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!enrollmentRepository.existsById(id)) {
            throw new EnrollmentNotFoundException("Matrícula no encontrada con ID: " + id);
        }
        enrollmentRepository.deleteById(id);
    }
}