package com.service.implement;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dto.*;
import com.dto.exam.ResultEntryDTO;
import com.entity.academic.*;
import com.entity.attendance.*;
import com.entity.users.*;
import com.exception.ResourceNotFoundException;
import com.repository.*;
import com.service.NotificationService;
import com.service.TeacherService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final AssignmentRepository assignmentRepository;
    private final AssignmentSubmissionRepository assignmentSubmissionRepository;
    private final ClassRoomRepository classRoomRepository;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;
    private final ExamRepository examRepository;
    private final ResultRepository resultRepository;
    private final NotificationService notificationService;

    private Teacher getTeacherByTeacherId(String teacherId) {
        return teacherRepository.findByTeacherId(teacherId)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with ID: " + teacherId));
    }

    @Override
    public ApiResponse postAssignment(String teacherId, AssignmentRequestDTO dto) {
        Teacher teacher = getTeacherByTeacherId(teacherId);

        ClassRoom classRoom = classRoomRepository.findById(dto.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));

        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        Assignment assignment = new Assignment();
        assignment.setTitle(dto.getTitle());
        assignment.setDescription(dto.getDescription());
        assignment.setDueDate(dto.getDueDate().atStartOfDay());
        assignment.setFileUrl(dto.getFileUrl());
        assignment.setTeacher(teacher);
        assignment.setClassRoom(classRoom);
        assignment.setSubject(subject);

        assignmentRepository.save(assignment);

        return new ApiResponse("Assignment posted successfully", true);
    }

    @Override
    public ApiResponse markAttendance(String teacherId, AttendanceMarkDTO dto) {
        getTeacherByTeacherId(teacherId);

        ClassRoom classRoom = classRoomRepository.findById(dto.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));

        LocalDate date = LocalDate.parse(dto.getDate());

        for (AttendanceMarkDTO.AttendanceEntry entry : dto.getEntries()) {
            Student student = studentRepository.findByStudentId(entry.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

            Attendance attendance = new Attendance();
            attendance.setClassRoom(classRoom);
            attendance.setDate(date);
            attendance.setStudent(student);
            attendance.setStatus(AttendanceStatus.valueOf(entry.getStatus()));

            attendanceRepository.save(attendance);

            notificationService.sendNotification(
                student,
                "Attendance Marked",
                "Your attendance for " + date + " has been marked as " + entry.getStatus(),
                "ATTENDANCE"
            );
        }

        return new ApiResponse("Attendance marked successfully", true);
    }

    @Override
    public ApiResponse enterGrades(String teacherId, GradeEntryDTO dto) {
        getTeacherByTeacherId(teacherId);

        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        ClassRoom classRoom = classRoomRepository.findById(dto.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));

        Exam exam = new Exam();
        exam.setTitle(dto.getExamName());
        exam.setClassRoom(classRoom);
        exam.setSubject(subject);
        examRepository.save(exam);

        for (GradeEntryDTO.MarkEntry mark : dto.getMarks()) {
            Student student = studentRepository.findById(mark.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

            Result result = new Result();
            result.setExam(exam);
            result.setStudent(student);
            result.setMarksObtained(mark.getMarksObtained());

            resultRepository.save(result);
        }

        return new ApiResponse("Grades entered successfully", true);
    }

    @Override
    public ApiResponse getTimetable(String teacherId) {
        Teacher teacher = getTeacherByTeacherId(teacherId);
        return new ApiResponse("Timetable fetched successfully", true, teacher.getAssignedClasses());
    }

    @Override
    public ApiResponse getAssignedClassesAndSubjects(String teacherId) {
        Teacher teacher = getTeacherByTeacherId(teacherId);

        Map<String, Object> data = new HashMap<>();
        data.put("classes", teacher.getAssignedClasses());
        data.put("subjects", teacher.getSubjects());

        return new ApiResponse("Classes and subjects fetched", true, data);
    }

    @Override
    public ApiResponse publishResult(ResultEntryDTO dto) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Exam exam = examRepository.findById(dto.getExamId())
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

        Result result = new Result();
        result.setStudent(student);
        result.setExam(exam);
        result.setMarksObtained(dto.getMarksObtained());
        result.setGrade(dto.getGrade());
        result.setRemarks(dto.getRemarks());

        resultRepository.save(result);

        notificationService.sendNotification(
            student,
            "Result Published",
            "Your result for exam has been published. Marks: " + dto.getMarksObtained() + ", Grade: " + dto.getGrade(),
            "RESULT"
        );

        return new ApiResponse("Result published successfully", true);
    }

    @Override
    public ApiResponse getExamsByClass(Long classId) {
        ClassRoom classRoom = classRoomRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));

        List<Exam> exams = examRepository.findByClassRoom(classRoom);

        List<Map<String, Object>> data = exams.stream().map(exam -> {
            Map<String, Object> map = new HashMap<>();
            map.put("title", exam.getTitle());
            map.put("date", exam.getExamDate());
            map.put("term", exam.getTerm());
            map.put("subject", exam.getSubject().getName());
            return map;
        }).collect(Collectors.toList());

        return new ApiResponse("Exams fetched for the class", true, data);
    }

    @Override
    public ApiResponse getTeacherDashboard(String teacherId) {
        Teacher teacher = getTeacherByTeacherId(teacherId);

        long classCount = teacher.getAssignedClasses().size();
        long subjectCount = teacher.getSubjects().size();
        long assignmentCount = assignmentRepository.countByTeacher(teacher);

        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("teacherId", teacher.getTeacherId());
        dashboard.put("name", teacher.getFullName());
        dashboard.put("classesHandled", classCount);
        dashboard.put("subjectsHandled", subjectCount);
        dashboard.put("assignmentsGiven", assignmentCount);

        return new ApiResponse("Teacher dashboard fetched", true, dashboard);
    }

    @Override
    public ApiResponse getAssignmentSubmissionTracker(String teacherId, Long assignmentId) {
        getTeacherByTeacherId(teacherId);
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));
        List<Student> allStudents = studentRepository.findByClassRoomId(assignment.getClassRoom().getId());
        List<AssignmentSubmission> submissions = assignmentSubmissionRepository.findByAssignment(assignment);
        List<String> submittedIds = submissions.stream()
                .map(s -> s.getStudent().getStudentId()).collect(Collectors.toList());
        List<Map<String, Object>> tracker = allStudents.stream().map(student -> {
            Map<String, Object> entry = new HashMap<>();
            entry.put("studentId", student.getStudentId());
            entry.put("name", student.getFullName());
            entry.put("submitted", submittedIds.contains(student.getStudentId()));
            submissions.stream()
                    .filter(s -> s.getStudent().getStudentId().equals(student.getStudentId()))
                    .findFirst()
                    .ifPresent(s -> entry.put("submittedAt", s.getSubmittedAt()));
            return entry;
        }).collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>();
        result.put("assignmentTitle", assignment.getTitle());
        result.put("dueDate", assignment.getDueDate());
        result.put("totalStudents", allStudents.size());
        result.put("submittedCount", submittedIds.size());
        result.put("pendingCount", allStudents.size() - submittedIds.size());
        result.put("tracker", tracker);
        return new ApiResponse("Submission tracker fetched", true, result);
    }
}
