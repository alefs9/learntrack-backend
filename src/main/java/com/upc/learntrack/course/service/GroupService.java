package com.upc.learntrack.course.service;

import com.upc.learntrack.course.dto.GroupDto;
import com.upc.learntrack.course.dto.GroupDetailsDto;
import com.upc.learntrack.course.dto.StudentDto;
import com.upc.learntrack.course.dto.StudentSimpleDto;
import com.upc.learntrack.course.dto.StudentStatDto;
import java.util.List;

public interface GroupService {
    List<GroupDto> findAll();
    GroupDto findById(Long id);
    List<GroupDto> findAllMyGroups(String teacherEmail);
    GroupDto save(GroupDto dto, String teacherEmail);
    List<StudentDto> findStudentsByGroupId(Long groupId);
    void setTopicPriority(Long groupId, Long topicId, boolean priority);
    List<StudentDto> enrollStudents(String groupCode, List<String> studentEmails, String teacherEmail);
    GroupDetailsDto findByCode(String code);
    List<StudentSimpleDto> findStudentsByGroupCode(String code);
    StudentSimpleDto enrollStudent(String groupCode, String studentEmail, String teacherEmail);
    List<StudentStatDto> getStudentStats(String groupCode);
    GroupDto update(Long id, GroupDto dto, String teacherEmail);
    void removeStudentFromGroup(String groupCode, String studentEmail, String teacherEmail);
}