package com.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.dto.ApiResponse;
import com.dto.RegisterParentDTO;
import com.dto.RegisterStudentDTO;
import com.dto.RegisterTeacherDTO;
import com.dto.SubjectDTO;


public interface AdminService {

    ApiResponse registerStudent(RegisterStudentDTO dto);
    ApiResponse registerTeacher(RegisterTeacherDTO dto);
    ApiResponse registerParent(RegisterParentDTO dto);
    ApiResponse uploadDocument(Long userId, String name, MultipartFile fileUrl);
    ApiResponse createClass(String className, String section);
    ApiResponse assignTeacherToClass(Long teacherId, Long classId);
    ApiResponse assignSubjectToClass(Long subjectId, Long classId);
    ApiResponse createSubject(SubjectDTO dto);
    ApiResponse getAdminDashboardSummary();
    ApiResponse getAllStudents();
    ApiResponse getAllTeachers();
    ApiResponse getAllClasses();
    ApiResponse getAllSubjects();
    ApiResponse getAllParents();
    ApiResponse updateStudent(Long id, Map<String, Object> updates);
    ApiResponse toggleStudentActiveStatus(Long studentId);
    ApiResponse assignStudentToClass(Long studentId, Long classId);
    ApiResponse getStudentById(Long id);
    ApiResponse getStudentDocuments(Long id);
    ApiResponse getAllDocuments();
    
    ApiResponse getTeacherByTeacherId(Long teacherId);

    ApiResponse updateTeacher(Long id, Map<String, Object> updates);
    ApiResponse toggleTeacherActiveStatus(Long teacherId);
	ApiResponse getTeacherDocuments(Long teacherId);
	ApiResponse assignSubjectToTeacher(Long teacherId, Long subjectId);
	ApiResponse getParentById(Long id);
    ApiResponse getAttendanceSummaryByClass(Long classId);
    ApiResponse getFeeCollectionReport();
    ApiResponse searchStudents(String name, Long classId, Boolean active);
    ApiResponse getExamScheduleByClass(Long classId);
    ApiResponse getResultSummaryByTerm(String studentId, String term);
    ApiResponse searchTeachers(String name, String department);
    ApiResponse getClassWiseStudentCount();
    ApiResponse getSubjectWiseResultAnalysis(Long classId);
    ApiResponse deleteStudent(Long id);
    ApiResponse deleteTeacher(Long id);
    ApiResponse updateClass(Long classId, String className, String section);
    ApiResponse getStudentProfile(String studentId);
    ApiResponse getEnhancedDashboard();
}
