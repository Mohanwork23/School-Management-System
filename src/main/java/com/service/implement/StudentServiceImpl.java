package com.service.implement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dto.ApiResponse;
import com.dto.AssignmentDTO;
import com.dto.AttendanceDTO;
import com.dto.ResultDTO;
import com.entity.academic.Assignment;
import com.entity.academic.AssignmentSubmission;
import com.entity.academic.Exam;
import com.entity.academic.Result;
import com.entity.attendance.Attendance;
import com.entity.fees.FeePayment;
import com.entity.users.Student;
import com.exception.ResourceNotFoundException;
import com.repository.AssignmentRepository;
import com.repository.AssignmentSubmissionRepository;
import com.repository.AttendanceRepository;
import com.repository.ExamRepository;
import com.repository.ResultRepository;
import com.repository.StudentRepository;
import com.repository.TimeTableEntryRepository;
import com.repository.fees.FeePaymentRepository;
import com.service.StudentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final AssignmentRepository assignmentRepository;
    private final AssignmentSubmissionRepository assignmentSubmissionRepository;
    private final AttendanceRepository attendanceRepository;
    private final ResultRepository resultRepository;
    private final FeePaymentRepository feePaymentRepository;
    private final TimeTableEntryRepository timeTableEntryRepository;
    private final ExamRepository examRepository;
    private final PasswordEncoder passwordEncoder;

    private Student getStudent(String studentId) {
        return studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));
    }

    @Override
    public ApiResponse getTimeTableForStudent(String studentId) {
        Student student = getStudent(studentId);
        if (student.getClassRoom() == null)
            return new ApiResponse("No class assigned to this student", false);
        Long classId = student.getClassRoom().getId();
        return new ApiResponse("Timetable fetched", true,
                timeTableEntryRepository.findByClassRoomIdOrderByDayOfWeekAscPeriodAsc(classId));
    }

    @Override
    public ApiResponse getGradesForStudent(String studentId) {
        Student student = getStudent(studentId);
        List<Result> results = resultRepository.findByStudent(student);

        List<ResultDTO> dtos = results.stream().map(result -> {
            ResultDTO dto = new ResultDTO();
            dto.setExamTitle(result.getExam().getTitle());
            dto.setSubjectName(result.getExam().getSubject().getName());
            dto.setMarksObtained(result.getMarksObtained());
            dto.setTerm(result.getExam().getTerm());
            dto.setAcademicYear(result.getExam().getAcademicYear());
            return dto;
        }).collect(Collectors.toList());

        return new ApiResponse("Grades fetched", true, dtos);
    }

    @Override
    public ApiResponse getAssignmentsForStudent(String studentId) {
        Student student = getStudent(studentId);
        List<Assignment> assignments = assignmentRepository.findByClassRoom(student.getClassRoom());

        List<AssignmentDTO> dtos = assignments.stream().map(a -> {
            AssignmentDTO dto = new AssignmentDTO();
            dto.setId(a.getId());
            dto.setTitle(a.getTitle());
            dto.setDescription(a.getDescription());
            dto.setDueDate(a.getDueDate().toString());
            dto.setSubjectName(a.getSubject().getName());
            dto.setFileUrl(a.getFileUrl());
            return dto;
        }).collect(Collectors.toList());

        return new ApiResponse("Assignments fetched", true, dtos);
    }

    @Override
    public ApiResponse submitAssignment(String studentId, Long assignmentId, String fileUrl) {
        Student student = getStudent(studentId);
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with ID: " + assignmentId));

        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setFileUrl(fileUrl);

        assignmentSubmissionRepository.save(submission);
        return new ApiResponse("Assignment submitted", true);
    }

    @Override
    public ApiResponse getAttendanceForStudent(String studentId) {
        Student student = getStudent(studentId);
        List<Attendance> records = attendanceRepository.findByStudent(student);

        List<AttendanceDTO> dtos = records.stream().map(a -> {
            AttendanceDTO dto = new AttendanceDTO();
            dto.setDate(a.getDate().toString());
            dto.setStatus(a.getStatus());
            dto.setRemarks(a.getRemarks() != null ? a.getRemarks() : "");
            return dto;
        }).collect(Collectors.toList());

        return new ApiResponse("Attendance fetched", true, dtos);
    }

    @Override
    public ApiResponse getFeeStatus(String studentId) {
        Student student = getStudent(studentId);
        List<FeePayment> payments = feePaymentRepository.findByStudentId(student.getId());
        double totalPaid = payments.stream().mapToDouble(FeePayment::getAmountPaid).sum();
        Map<String, Object> feeStatus = new HashMap<>();
        feeStatus.put("studentId", studentId);
        feeStatus.put("studentName", student.getFullName());
        feeStatus.put("totalPaid", totalPaid);
        feeStatus.put("payments", payments);
        return new ApiResponse("Fee status fetched", true, feeStatus);
    }

    @Override
    public ApiResponse getResults(String studentId) {
        Student student = getStudent(studentId);
        List<Result> results = resultRepository.findByStudent(student);

        List<ResultDTO> dtos = results.stream().map(result -> {
            ResultDTO dto = new ResultDTO();
            dto.setExamTitle(result.getExam().getTitle());
            dto.setSubjectName(result.getExam().getSubject().getName());
            dto.setMarksObtained(result.getMarksObtained());
            dto.setTerm(result.getExam().getTerm());
            dto.setAcademicYear(result.getExam().getAcademicYear());
            dto.setStudentName(student.getFullName());
            return dto;
        }).collect(Collectors.toList());

        return new ApiResponse("Results fetched successfully", true, dtos);
    }

    @Override
    public ApiResponse getReportCard(String studentId) {
        Student student = getStudent(studentId);

        Map<String, Object> reportCard = new HashMap<>();
        reportCard.put("studentId", student.getStudentId());
        reportCard.put("name", student.getFullName());
        reportCard.put("dob", student.getDateOfBirth());
        reportCard.put("gender", student.getGender());
        reportCard.put("class", student.getClassRoom() != null ? student.getClassRoom().getClassName() : "Not Assigned");

        List<Result> results = resultRepository.findByStudent(student);
        List<Map<String, Object>> grades = results.stream().map(result -> {
            Map<String, Object> grade = new HashMap<>();
            grade.put("subject", result.getExam().getSubject().getName());
            grade.put("examTitle", result.getExam().getTitle());
            grade.put("marks", result.getMarksObtained());
            grade.put("grade", result.getGrade());
            grade.put("remarks", result.getRemarks());
            grade.put("term", result.getExam().getTerm());
            grade.put("academicYear", result.getExam().getAcademicYear());
            return grade;
        }).collect(Collectors.toList());

        reportCard.put("grades", grades);

        List<Attendance> attendanceList = attendanceRepository.findByStudent(student);
        long presentCount = attendanceList.stream()
                .filter(a -> a.getStatus().toString().equalsIgnoreCase("PRESENT")).count();
        long totalDays = attendanceList.size();
        double attendancePercentage = totalDays > 0 ? (presentCount * 100.0 / totalDays) : 0.0;

        reportCard.put("attendance", Map.of(
                "totalDays", totalDays,
                "presentDays", presentCount,
                "attendancePercentage", String.format("%.2f", attendancePercentage) + "%"
        ));

        return new ApiResponse("Student report card generated successfully", true, reportCard);
    }

    @Override
    public ApiResponse getAssignmentSubmissionProgress(String studentId) {
        Student student = getStudent(studentId);
        long totalAssignments = assignmentRepository.countByClassRoom(student.getClassRoom());
        long submittedAssignments = assignmentSubmissionRepository.findByStudent(student)
                .stream()
                .map(AssignmentSubmission::getAssignment)
                .distinct()
                .count();

        Map<String, Object> progress = new HashMap<>();
        progress.put("totalAssignments", totalAssignments);
        progress.put("submittedAssignments", submittedAssignments);
        progress.put("submissionPercentage",
                totalAssignments > 0 ? String.format("%.2f", (submittedAssignments * 100.0 / totalAssignments)) + "%" : "0%");

        return new ApiResponse("Assignment submission progress fetched", true, progress);
    }

    @Override
    public ApiResponse getUpcomingExams(String studentId) {
        Student student = getStudent(studentId);
        if (student.getClassRoom() == null)
            return new ApiResponse("No class assigned", false);
        List<Exam> exams = examRepository.findByClassRoomIdOrderByExamDateAsc(student.getClassRoom().getId());
        List<Map<String, Object>> upcoming = exams.stream()
                .filter(e -> e.getExamDate() != null && !e.getExamDate().isBefore(java.time.LocalDate.now()))
                .map(e -> {
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("title", e.getTitle());
                    entry.put("subject", e.getSubject() != null ? e.getSubject().getName() : "N/A");
                    entry.put("date", e.getExamDate());
                    entry.put("term", e.getTerm());
                    return entry;
                }).collect(Collectors.toList());
        return new ApiResponse("Upcoming exams fetched", true, upcoming);
    }

    @Override
    public ApiResponse getStudentProfile(String studentId) {
        Student student = getStudent(studentId);
        Map<String, Object> profile = new HashMap<>();
        profile.put("studentId", student.getStudentId());
        profile.put("name", student.getFullName());
        profile.put("email", student.getEmail());
        profile.put("phone", student.getPhone());
        profile.put("gender", student.getGender());
        profile.put("dob", student.getDateOfBirth());
        profile.put("class", student.getClassRoom() != null ?
                student.getClassRoom().getClassName() + " - " + student.getClassRoom().getSection() : "Not Assigned");
        profile.put("active", student.isActive());
        return new ApiResponse("Student profile fetched", true, profile);
    }

    @Override
    public ApiResponse changePassword(String studentId, String oldPassword, String newPassword) {
        Student student = getStudent(studentId);
        if (!passwordEncoder.matches(oldPassword, student.getPassword()))
            return new ApiResponse("Old password is incorrect", false);
        student.setPassword(passwordEncoder.encode(newPassword));
        studentRepository.save(student);
        return new ApiResponse("Password changed successfully", true);
    }

    @Override
    public ApiResponse getStudentDashboard(String studentId) {
        Student student = getStudent(studentId);
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("studentId", student.getStudentId());
        dashboard.put("name", student.getFullName());
        dashboard.put("class", student.getClassRoom() != null ? student.getClassRoom().getClassName() : "Not Assigned");

        List<Assignment> assignments = assignmentRepository.findByClassRoom(student.getClassRoom());
        long submitted = assignmentSubmissionRepository.findByStudent(student).stream()
                .map(AssignmentSubmission::getAssignment).distinct().count();
        dashboard.put("totalAssignments", assignments.size());
        dashboard.put("submittedAssignments", submitted);
        dashboard.put("pendingAssignments", assignments.size() - submitted);

        List<Attendance> attendanceList = attendanceRepository.findByStudent(student);
        long present = attendanceList.stream()
                .filter(a -> a.getStatus().toString().equalsIgnoreCase("PRESENT")).count();
        long total = attendanceList.size();
        dashboard.put("attendancePercentage",
                total > 0 ? String.format("%.2f", (present * 100.0 / total)) + "%" : "0%");

        List<FeePayment> payments = feePaymentRepository.findByStudentId(student.getId());
        dashboard.put("totalFeePaid", payments.stream().mapToDouble(FeePayment::getAmountPaid).sum());

        return new ApiResponse("Student dashboard fetched", true, dashboard);
    }
}
