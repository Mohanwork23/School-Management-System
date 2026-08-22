package com.service.admin;

import com.dto.ApiResponse;
import com.dto.RegisterStudentDTO;
import com.dto.RegisterTeacherDTO;
import com.email.service.EmailService;
import com.entity.academic.ClassRoom;
import com.entity.academic.Subject;
import com.entity.users.Student;
import com.entity.users.Teacher;
import com.exception.ResourceNotFoundException;
import com.repository.AttendanceRepository;
import com.repository.ClassRoomRepository;
import com.repository.DocumentRepository;
import com.repository.ExamRepository;
import com.repository.ParentRepository;
import com.repository.ResultRepository;
import com.repository.StudentRepository;
import com.repository.SubjectRepository;
import com.repository.TeacherRepository;
import com.repository.UserRepository;
import com.repository.fees.FeePaymentRepository;
import com.service.implement.AdminServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock private StudentRepository studentRepository;
    @Mock private TeacherRepository teacherRepository;
    @Mock private ParentRepository parentRepository;
    @Mock private ClassRoomRepository classRoomRepository;
    @Mock private SubjectRepository subjectRepository;
    @Mock private UserRepository userRepository;
    @Mock private DocumentRepository documentRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private EmailService emailService;
    @Mock private AttendanceRepository attendanceRepository;
    @Mock private FeePaymentRepository feePaymentRepository;
    @Mock private ExamRepository examRepository;
    @Mock private ResultRepository resultRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(adminService, "maxDocumentSizeBytes", 5242880L);
        ReflectionTestUtils.setField(adminService, "allowedDocumentContentTypes",
                "application/pdf,image/jpeg,image/png");
    }

    @Test
    void registerStudent_savesStudentAndSendsEmail() {
        ClassRoom classRoom = new ClassRoom();
        classRoom.setClassName("Grade 10");

        RegisterStudentDTO dto = new RegisterStudentDTO();
        dto.setFullName("John Doe");
        dto.setEmail("john@school.com");
        dto.setPhone("9876543210");
        dto.setGender("Male");
        dto.setDob("2005-06-15");
        dto.setClassRoomId(1L);

        when(studentRepository.count()).thenReturn(0L);
        when(classRoomRepository.findById(1L)).thenReturn(Optional.of(classRoom));
        when(passwordEncoder.encode("default123")).thenReturn("encodedPass");
        when(studentRepository.save(any(Student.class))).thenAnswer(i -> i.getArgument(0));

        ApiResponse response = adminService.registerStudent(dto);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).contains("STU");

        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(captor.capture());
        Student saved = captor.getValue();
        assertThat(saved.getFullName()).isEqualTo("John Doe");
        assertThat(saved.getPassword()).isEqualTo("encodedPass");
        assertThat(saved.getClassRoom()).isEqualTo(classRoom);

        verify(emailService).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void registerStudent_classNotFound_throwsException() {
        RegisterStudentDTO dto = new RegisterStudentDTO();
        dto.setFullName("Jane");
        dto.setEmail("jane@school.com");
        dto.setPhone("9876543210");
        dto.setGender("Female");
        dto.setDob("2005-01-01");
        dto.setClassRoomId(99L);

        when(studentRepository.count()).thenReturn(0L);
        when(classRoomRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminService.registerStudent(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Class not found");
    }

    @Test
    void registerTeacher_savesTeacherWithSubjectsAndSendsEmail() {
        Subject subject = new Subject();
        subject.setName("Physics");

        RegisterTeacherDTO dto = new RegisterTeacherDTO();
        dto.setFullName("Mr. Smith");
        dto.setEmail("smith@school.com");
        dto.setPhone("9876543211");
        dto.setDepartment("Science");
        dto.setQualification("M.Sc");
        dto.setSubjectIds(List.of(1L));
        dto.setClassIds(List.of());

        when(teacherRepository.count()).thenReturn(2L);
        when(subjectRepository.findAllById(List.of(1L))).thenReturn(List.of(subject));
        when(classRoomRepository.findAllById(List.of())).thenReturn(List.of());
        when(passwordEncoder.encode("default123")).thenReturn("encodedPass");
        when(teacherRepository.save(any(Teacher.class))).thenAnswer(i -> i.getArgument(0));

        ApiResponse response = adminService.registerTeacher(dto);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).contains("TEA");

        ArgumentCaptor<Teacher> captor = ArgumentCaptor.forClass(Teacher.class);
        verify(teacherRepository).save(captor.capture());
        Teacher saved = captor.getValue();
        assertThat(saved.getFullName()).isEqualTo("Mr. Smith");
        assertThat(saved.getDepartment()).isEqualTo("Science");
        assertThat(saved.getSubjects()).containsExactly(subject);

        verify(emailService).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void deleteStudent_studentExists_deletesSuccessfully() {
        Student student = new Student();
        student.setStudentId("STU001");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        ApiResponse response = adminService.deleteStudent(1L);

        assertThat(response.isSuccess()).isTrue();
        verify(studentRepository).delete(student);
    }

    @Test
    void deleteStudent_studentNotFound_throwsException() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminService.deleteStudent(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void toggleStudentActiveStatus_togglesCorrectly() {
        Student student = new Student();
        student.setActive(true);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        ApiResponse response = adminService.toggleStudentActiveStatus(1L);

        assertThat(response.isSuccess()).isTrue();
        assertThat(student.isActive()).isFalse();
    }
}
