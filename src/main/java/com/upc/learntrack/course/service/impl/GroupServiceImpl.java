package com.upc.learntrack.course.service.impl;

import com.upc.learntrack.course.dto.GroupDto;
import com.upc.learntrack.course.exception.GroupNotFoundException;
import com.upc.learntrack.course.exception.TeacherNotFoundException;
import com.upc.learntrack.course.mapper.GroupMapper;
import com.upc.learntrack.course.model.Group;
import com.upc.learntrack.course.model.Teacher;
import com.upc.learntrack.course.repository.GroupRepository;
import com.upc.learntrack.course.repository.TeacherRepository;
import com.upc.learntrack.course.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final TeacherRepository teacherRepository;
    private final GroupMapper groupMapper;

    @Override
    public List<GroupDto> findAll() {
        return groupRepository.findAll().stream()
                .map(groupMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupDto> findAllByTeacher(Long teacherId) {
        return groupRepository.findAllByTeacherId(teacherId).stream()
                .map(groupMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public GroupDto findById(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException("Grupo no encontrado con ID: " + id));
        return groupMapper.toDto(group);
    }

    @Override
    public GroupDto save(GroupDto dto) {
        if (groupRepository.existsByCode(dto.getCode())) {
            throw new IllegalArgumentException("El código de grupo '" + dto.getCode() + "' ya está en uso.");
        }

        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new TeacherNotFoundException("Profesor no encontrado con ID: " + dto.getTeacherId()));

        Group group = groupMapper.toEntity(dto);
        group.setTeacher(teacher);
        return groupMapper.toDto(groupRepository.save(group));
    }
}