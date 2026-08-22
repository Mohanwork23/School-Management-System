package com.service;

import com.dto.ApiResponse;
import com.entity.academic.Assignment;
import com.entity.academic.AssignmentSubmission;
import com.entity.academic.ClassRoom;
import com.entity.academic.Exam;
import com.entity.academic.Result;
import com.entity.academic.Subject;
import com.entity.attendance.Attendance;
import com.entity.attendance.AttendanceStatus;
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
import com.service.implement.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock private StudentRepository studentRepository;
    @Mock private AssignmentRepository assignmentRepository;
    @Mock private AssignmentSubmissionRepository assignmentSubmissionRepository;
    @Mock private AttendanceRepository attendanceRepository;
    @Mock private ResultRepository resultRepository;
    @Mock private FeePaymentRepository feePaymentRepository;
    @Mock private TimeTableEntryRepository timeTableEntryRepository;
    @Mock private ExamRepository examRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;
    private ClassRoom classRoom;

    @BeforeEach
    void setUp() {
        classRoom = new ClassRoom();
        classRoom.setClassName("Grade 10");
        classRoom.setSection("A");

        student = new Student();
        student.setStudentId("STU001");
        student.setFullName("Test Student");
        student.setClassRoom(classRoom);
        student.setPassword("encodedPassword");
    }

    @Test
    void getStudentDashboard_returnsCorrectSummary() {
        Assignment a1 = new Assignment();
        Assignment a2 = new Assignment();

        AssignmentSubmission sub = new AssignmentSubmission();
        sub.setAssignment(a1);

        Attendance present = new Attendance();
        present.setStatus(AttendanceStatus.PRESENT);
        Attendance absent = new Attendance();
        absent.setStatus(AttendanceStatus.ABSENT);

        FeePayment payment = new FeePayment();
        payment.setAmountPaid(5000.0);

        when(studentRepository.findByStudentId("STU001")).thenReturn(Optional.of(student));
        when(assignmentRepository.findByClassRoom(classRoom)).thenReturn(List.of(a1, a2));
        when(assignmentSubmissionRepository.findByStudent(student)).thenReturn(List.of(sub));
        when(attendanceRepository.findByStudent(student)).thenReturn(List.of(present, absent));
        when(feePaymentRepository.findByStudentId(student.getId())).thenReturn(List.of(payment));

        ApiResponse response = studentService.getStudentDashboard("STU001");

        assertThat(response.isSuccess()).isTrue();
        Map<?, ?> data = (Map<?, ?>) response.getData();
        assertThat(data.get("totalAssignments")).isEqualTo(2);
        assertThat(data.get("submittedAssignments")).isEqualTo(1L);
        assertThat(data.get("pendingAssignments")).isEqualTo(1L);
        assertThat(data.get("attendancePercentage")).isEqualTo("50.00%");
        assertThat(data.get("totalFeePaid")).isEqualTo(5000.0);
    }

    @Test
    void getStudentDashboard_studentNotFound_throwsException() {
        when(studentRepository.findByStudentId("INVALID")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getStudentDashboard("INVALID"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("INVALID");
    }

    @Test
    void getReportCard_returnsGradesAndAttendance() {
        Subject subject = new Subject();
        subject.setName("Mathematics");

        Exam exam = new Exam();
        exam.setTitle("Mid Term");
        exam.setSubject(subject);
        exam.setTerm("Term 1");
        exam.setAcademicYear("2025-26");

        Result result = new Result();
        result.setExam(exam);
        result.setMarksObtained(85.0);
        result.setGrade("A");
        result.setRemarks("Excellent");

        Attendance present = new Attendance();
        present.setStatus(AttendanceStatus.PRESENT);

        when(studentRepository.findByStudentId("STU001")).thenReturn(Optional.of(student));
        when(resultRepository.findByStudent(student)).thenReturn(List.of(result));
        when(attendanceRepository.findByStudent(student)).thenReturn(List.of(present));

        ApiResponse response = studentService.getReportCard("STU001");

        assertThat(response.isSuccess()).isTrue();
        Map<?, ?> data = (Map<?, ?>) response.getData();
        assertThat(data.get("name")).isEqualTo("Test Student");
        List<?> grades = (List<?>) data.get("grades");
        assertThat(grades).hasSize(1);
        Map<?, ?> attendance = (Map<?, ?>) data.get("attendance");
        assertThat(attendance.get("attendancePercentage")).isEqualTo("100.00%");
    }

    @Test
    void getUpcomingExams_returnsOnlyFutureExams() {
        Exam future = new Exam();
        future.setTitle("Final Exam");
        future.setExamDate(LocalDate.now().plusDays(5));
        future.setTerm("Term 2");

        Exam past = new Exam();
        past.setTitle("Past Exam");
        past.setExamDate(LocalDate.now().minusDays(3));

        when(studentRepository.findByStudentId("STU001")).thenReturn(Optional.of(student));
        when(examRepository.findByClassRoomIdOrderByExamDateAsc(classRoom.getId()))
                .thenReturn(List.of(past, future));

        ApiResponse response = studentService.getUpcomingExams("STU001");

        assertThat(response.isSuccess()).isTrue();
        List<?> exams = (List<?>) response.getData();
        assertThat(exams).hasSize(1);
        Map<?, ?> examData = (Map<?, ?>) exams.get(0);
        assertThat(examData.get("title")).isEqualTo("Final Exam");
    }

    @Test
    void getUpcomingExams_noClassAssigned_returnsFailure() {
        student.setClassRoom(null);
        when(studentRepository.findByStudentId("STU001")).thenReturn(Optional.of(student));

        ApiResponse response = studentService.getUpcomingExams("STU001");

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo("No class assigned");
    }

    @Test
    void changePassword_wrongOldPassword_returnsFailure() {
        when(studentRepository.findByStudentId("STU001")).thenReturn(Optional.of(student));
        when(passwordEncoder.matches("wrongPass", "encodedPassword")).thenReturn(false);

        ApiResponse response = studentService.changePassword("STU001", "wrongPass", "newPass123");

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo("Old password is incorrect");
    }

    @Test
    void changePassword_correctOldPassword_updatesPassword() {
        when(studentRepository.findByStudentId("STU001")).thenReturn(Optional.of(student));
        when(passwordEncoder.matches("correctPass", "encodedPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPass123")).thenReturn("newEncodedPassword");

        ApiResponse response = studentService.changePassword("STU001", "correctPass", "newPass123");

        assertThat(response.isSuccess()).isTrue();
        assertThat(student.getPassword()).isEqualTo("newEncodedPassword");
    }
}
