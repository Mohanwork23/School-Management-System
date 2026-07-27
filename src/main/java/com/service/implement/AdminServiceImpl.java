package com.service.implement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dto.ApiResponse;
import com.dto.ClassRoomDTO;
import com.dto.DocumentDTO;
import com.dto.RegisterParentDTO;
import com.dto.RegisterStudentDTO;
import com.dto.RegisterTeacherDTO;
import com.dto.StudentResponseDTO;
import com.dto.SubjectDTO;
import com.dto.TeacherResponseDTO;
import com.email.service.EmailService;
import com.entity.academic.ClassRoom;
import com.entity.academic.Exam;
import com.entity.academic.Result;
import com.entity.academic.Subject;
import com.entity.attendance.Attendance;
import com.entity.attendance.AttendanceStatus;
import com.entity.enums.Role;
import com.entity.users.Document;
import com.entity.users.Parent;
import com.entity.users.Student;
import com.entity.users.Teacher;
import com.entity.users.User;
import com.repository.AttendanceRepository;
import com.repository.ClassRoomRepository;
import com.repository.DocumentRepository;
import com.repository.ExamRepository;
import com.repository.ResultRepository;
import com.repository.fees.FeePaymentRepository;
import com.repository.ParentRepository;
import com.repository.StudentRepository;
import com.repository.SubjectRepository;
import com.repository.TeacherRepository;
import com.repository.UserRepository;
import com.service.AdminService;
import com.util.IdGeneratorUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ParentRepository parentRepository;
    private final ClassRoomRepository classRoomRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final EmailService emailService;
    private final AttendanceRepository attendanceRepository;
    private final FeePaymentRepository feePaymentRepository;
    private final ExamRepository examRepository;
    private final ResultRepository resultRepository;

    @Override
    public ApiResponse registerStudent(RegisterStudentDTO dto) {
        Student student = new Student();
        student.setFullName(dto.getFullName());
        student.setEmail(dto.getEmail());
        student.setPhone(dto.getPhone());
        student.setGender(dto.getGender());
        student.setDateOfBirth(LocalDate.parse(dto.getDob()));
        student.setRole(Role.STUDENT);

        String generatedId = IdGeneratorUtil.generateStudentId(studentRepository.count());
        student.setStudentId(generatedId);
        student.setUsername(generatedId);
        student.setPassword(passwordEncoder.encode("default123"));

        ClassRoom classRoom = classRoomRepository.findById(dto.getClassRoomId())
                .orElseThrow(() -> new RuntimeException("Class not found"));
        student.setClassRoom(classRoom);

        if (dto.getParentUsername() != null) {
            Parent parent = parentRepository.findByUsername(dto.getParentUsername())
                    .orElseThrow(() -> new RuntimeException("Parent not found"));
            student.setParent(parent);
        }

        studentRepository.save(student);

        emailService.sendEmail(
            student.getEmail(),
            "Welcome to School Portal",
            "Dear " + student.getFullName() + ",\n\nYou have been successfully registered with ID: " + generatedId + 
            ".\nYour username: " + generatedId + "\nPassword: default123\n\nRegards,\nAdmin Team"
        );

        return new ApiResponse("Student registered with ID: " + generatedId, true);
    }


    @Override
    public ApiResponse registerTeacher(RegisterTeacherDTO dto) {
        Teacher teacher = new Teacher();
        teacher.setFullName(dto.getFullName());
        teacher.setEmail(dto.getEmail());
        teacher.setPhone(dto.getPhone());
        teacher.setDepartment(dto.getDepartment());
        teacher.setQualification(dto.getQualification());
        teacher.setRole(Role.TEACHER);

        String generatedId = IdGeneratorUtil.generateTeacherId(teacherRepository.count());
        teacher.setTeacherId(generatedId);
        teacher.setUsername(generatedId);
        teacher.setPassword(passwordEncoder.encode("default123"));

        List<Subject> subjects = subjectRepository.findAllById(dto.getSubjectIds());
        teacher.setSubjects(subjects);

        List<ClassRoom> classes = classRoomRepository.findAllById(dto.getClassIds());
        teacher.setAssignedClasses(classes);

        teacherRepository.save(teacher);

        emailService.sendEmail(
            teacher.getEmail(),
            "Welcome to School Portal",
            "Dear " + teacher.getFullName() + ",\n\nYou have been successfully registered with ID: " + generatedId + 
            ".\nYour username: " + generatedId + "\nPassword: default123\n\nRegards,\nAdmin Team"
        );

        return new ApiResponse("Teacher registered with ID: " + generatedId, true);
    }


    @Override
    public ApiResponse registerParent(RegisterParentDTO dto) {
        long counter = parentRepository.count();
        String generatedId;

        do {
            generatedId = IdGeneratorUtil.generateParentId(++counter);
        } while (parentRepository.findByUsername(generatedId).isPresent());

        Parent parent = new Parent();
        parent.setFullName(dto.getFullName());
        parent.setEmail(dto.getEmail());
        parent.setPhone(dto.getPhone());
        parent.setUsername(generatedId);
        parent.setPassword(passwordEncoder.encode("default123"));
        parent.setRole(Role.PARENT);
        parent.setActive(true);
        parent.setParentId(generatedId);
        parent.setAddress(dto.getAddress());
        parent.setGender(dto.getGender());

        parentRepository.save(parent);

        emailService.sendEmail(
            parent.getEmail(),
            "Welcome to School Portal",
            "Dear " + parent.getFullName() + ",\n\nYou have been successfully registered with ID: " + generatedId +
            ".\nYour username: " + generatedId + "\nPassword: default123\n\nRegards,\nAdmin Team"
        );

        return new ApiResponse("Parent registered with ID: " + generatedId, true);
    }
    @Override
    public ApiResponse uploadDocument(Long userId, String name, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            Document document = new Document();
            document.setName(name);
            document.setFileName(file.getOriginalFilename());
            document.setContentType(file.getContentType());
            document.setFileSize(file.getSize());
            document.setFileContent(file.getBytes());
            document.setUploadedBy(user);
            document.setAssociatedWith(user.getRole().name());

            documentRepository.save(document);
            return new ApiResponse("Document uploaded successfully", true);
        } catch (java.io.IOException e) {
            throw new RuntimeException("File processing failed", e);
        }
    }

    @Override
    public ApiResponse createClass(String className, String section) {
        ClassRoom classRoom = new ClassRoom();
        classRoom.setClassName(className);
        classRoom.setSection(section);
        classRoomRepository.save(classRoom);

        return new ApiResponse("Class created", true, classRoom);
    }

    @Override
    public ApiResponse assignTeacherToClass(Long teacherId, Long classId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        ClassRoom classRoom = classRoomRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        teacher.getAssignedClasses().add(classRoom);
        teacherRepository.save(teacher);

        return new ApiResponse("Teacher assigned to class", true);
    }

    @Override
    public ApiResponse assignSubjectToClass(Long subjectId, Long classId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        ClassRoom classRoom = classRoomRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        classRoom.getSubjects().add(subject);
        classRoomRepository.save(classRoom);

        return new ApiResponse("Subject assigned to class", true);
    }

    @Override
    public ApiResponse createSubject(SubjectDTO dto) {
        if (subjectRepository.findByName(dto.getName()).isPresent()) {
            throw new RuntimeException("Subject with name '" + dto.getName() + "' already exists");
        }
        Subject subject = new Subject();
        subject.setName(dto.getName());
        subject.setCode(dto.getCode());
        subjectRepository.save(subject);

        return new ApiResponse("Subject created", true, subject);
    }

    @Override
    public ApiResponse getAdminDashboardSummary() {
        long studentCount = studentRepository.count();
        long teacherCount = teacherRepository.count();
        long classCount = classRoomRepository.count();

        Map<String, Object> summary = new HashMap<>();
        summary.put("students", studentCount);
        summary.put("teachers", teacherCount);
        summary.put("classes", classCount);

        return new ApiResponse("Dashboard summary fetched", true, summary);
    }

    @Transactional
    @Override
    public ApiResponse getAllStudents() {
        List<Student> students = studentRepository.findAll();

        List<StudentResponseDTO> studentDTOs = students.stream()
            .map(this::mapToDTO)  
            .toList();
        
        return new ApiResponse("Students fetched", true, studentDTOs);
    }
    private StudentResponseDTO mapToDTO(Student student) {
        String parentUsername = (student.getParent() != null) ? student.getParent().getUsername() : "Unknown";
        
        ClassRoom classRoom = student.getClassRoom();
        Long classRoomId = (classRoom != null) ? classRoom.getId() : null;
        
        String className = (classRoom != null) ? classRoom.getClassName() : "Unknown";
        String section = (classRoom != null) ? classRoom.getSection() : "Unknown";
        
        List<String> subjects = new ArrayList<>();
        if (classRoom != null && classRoom.getSubjects() != null) {
            for (Subject subject : classRoom.getSubjects()) {
                subjects.add(subject.getName());
            }
        }

        String role = (student.getRole() != null) ? student.getRole().name() : "Unknown";

        return new StudentResponseDTO(
            student.getId(),
            student.getStudentId(),
            student.getUsername(),
            student.getFullName(),
            student.getEmail(),
            student.getPhone(),
            role,  
            student.isActive(),
            student.getDateOfBirth() != null ? student.getDateOfBirth().toString() : null,  
            student.getGender(),
            parentUsername,
            classRoomId,
            className,
            section,
            subjects
        );
    }




    @Override
    public ApiResponse getAllTeachers() {
        List<Teacher> teachers = teacherRepository.findAll();
        List<TeacherResponseDTO> dtos = teachers.stream()
            .map(this::mapToDTO)
            .toList();
        return new ApiResponse("Teachers fetched", true, dtos);
    }

    private TeacherResponseDTO mapToDTO(Teacher teacher) {
        TeacherResponseDTO dto = new TeacherResponseDTO();
        dto.setId(teacher.getId());
        dto.setFullName(teacher.getFullName());
        dto.setEmail(teacher.getEmail());
        dto.setPhone(teacher.getPhone());
        dto.setDepartment(teacher.getDepartment());
        dto.setQualification(teacher.getQualification());
        dto.setTeacherId(teacher.getTeacherId());

        List<SubjectDTO> subjectDTOs = teacher.getSubjects().stream().map(subject -> {
            SubjectDTO sDto = new SubjectDTO();
            sDto.setName(subject.getName());
            sDto.setCode(subject.getCode());
            return sDto;
        }).toList();
        dto.setSubjects(subjectDTOs);

        List<ClassRoomDTO> classDTOs = teacher.getAssignedClasses().stream().map(c -> {
            ClassRoomDTO cDto = new ClassRoomDTO();
            cDto.setId(c.getId());
            cDto.setClassName(c.getClassName());
            cDto.setSection(c.getSection());
            return cDto;
        }).toList();
        dto.setAssignedClasses(classDTOs);

        return dto;
    }

    @Override
    public ApiResponse getAllClasses() {
        return new ApiResponse("Classes fetched", true, classRoomRepository.findAll());
    }

    @Override
    public ApiResponse getAllSubjects() {
        return new ApiResponse("Subjects fetched", true, subjectRepository.findAll());
    }

    @Override
    public ApiResponse getAllParents() {
        return new ApiResponse("Parents fetched", true, parentRepository.findAll());
    }

  

    @Override
    public ApiResponse updateStudent(Long id, Map<String, Object> updates) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (updates.containsKey("fullName")) {
            student.setFullName((String) updates.get("fullName"));
        }
        if (updates.containsKey("email")) {
            student.setEmail((String) updates.get("email"));
        }
        if (updates.containsKey("phone")) {
            student.setPhone((String) updates.get("phone"));
        }
        if (updates.containsKey("gender")) {
            student.setGender((String) updates.get("gender"));
        }
        if (updates.containsKey("dateOfBirth")) {
            student.setDateOfBirth(LocalDate.parse((String) updates.get("dateOfBirth")));
        }

        studentRepository.save(student);

        return new ApiResponse("Student updated successfully", true, student);
    }

    @Override
    public ApiResponse toggleStudentActiveStatus(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        student.setActive(!student.isActive());
        studentRepository.save(student);
        return new ApiResponse("Student active status updated", true, student.isActive());
    }

    @Override
    public ApiResponse assignStudentToClass(Long studentId, Long classId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
        ClassRoom classRoom = classRoomRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found with ID: " + classId));
        student.setClassRoom(classRoom);
        studentRepository.save(student);
        return new ApiResponse("Student assigned to class successfully", true);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));
        return new ApiResponse("Student fetched successfully", true, student);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse getStudentDocuments(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));

        User user = userRepository.findByUsername(student.getUsername()).orElse(null);
        if (user == null) {
            throw new RuntimeException("User entity not found for student with username: " + student.getUsername());
        }

        List<Document> documents = documentRepository.findByUploadedBy(user);
        List<DocumentDTO> dtoList = documents.stream().map(doc -> {
            DocumentDTO dto = new DocumentDTO();
            dto.setId(doc.getId());
            dto.setName(doc.getName());
            dto.setFileType(doc.getContentType());
            dto.setFileSize(doc.getFileSize());
            dto.setAssociatedWith(doc.getAssociatedWith());
            dto.setDescription(doc.getDescription());
            dto.setUploadedBy(doc.getUploadedBy() != null ? doc.getUploadedBy().getUsername() : null);
            dto.setFileName(doc.getFileName());
            dto.setDownloadUrl("/api/admin/documents/" + doc.getId() + "/download");
            return dto;
        }).toList();

        return new ApiResponse("Documents fetched successfully", true, dtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse getAllDocuments() {
        List<Document> documents = documentRepository.findAll();

        List<DocumentDTO> dtoList = documents.stream().map(doc -> {
            DocumentDTO dto = new DocumentDTO();
            dto.setId(doc.getId());
            dto.setName(doc.getName());
            dto.setFileType(doc.getContentType());
            dto.setFileSize(doc.getFileSize());
            dto.setAssociatedWith(doc.getAssociatedWith());
            dto.setDescription(doc.getDescription());
            dto.setUploadedBy(doc.getUploadedBy() != null ? doc.getUploadedBy().getUsername() : null);
            dto.setFileName(doc.getFileName());
            dto.setDownloadUrl("/api/admin/documents/" + doc.getId() + "/download");
            return dto;
        }).toList();

        return new ApiResponse("All documents fetched successfully", true, dtoList);
    }
    @Override
    @Transactional(readOnly = true)
    public ApiResponse getTeacherByTeacherId(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new RuntimeException("Teacher not found with teacherId: " + teacherId));

        TeacherResponseDTO dto = mapToDTO(teacher); 
        return new ApiResponse("Teacher fetched successfully", true, dto);
    }
  
    @Override
    public ApiResponse updateTeacher(Long id, Map<String, Object> updates) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        if (updates.containsKey("fullName")) {
            teacher.setFullName((String) updates.get("fullName"));
        }
        if (updates.containsKey("email")) {
            teacher.setEmail((String) updates.get("email"));
        }
        if (updates.containsKey("phone")) {
            teacher.setPhone((String) updates.get("phone"));
        }
        if (updates.containsKey("department")) {
            teacher.setDepartment((String) updates.get("department"));
        }
        if (updates.containsKey("qualification")) {
            teacher.setQualification((String) updates.get("qualification"));
        }

        if (updates.containsKey("subjectIds")) {
            List<Long> subjectIds = extractIdList(updates.get("subjectIds"));
            if (!subjectIds.isEmpty()) {
                List<Subject> subjects = subjectRepository.findAllById(subjectIds);
                teacher.setSubjects(subjects);
            } else {
                teacher.setSubjects(List.of()); 
            }
        }

        if (updates.containsKey("classIds")) {
            List<Long> classIds = extractIdList(updates.get("classIds"));
            if (!classIds.isEmpty()) {
                List<ClassRoom> classes = classRoomRepository.findAllById(classIds);
                teacher.setAssignedClasses(classes);
            } else {
                teacher.setAssignedClasses(List.of()); 
            }
        }

        teacherRepository.save(teacher);
        return new ApiResponse("Teacher updated successfully", true, teacher);
    }


   
   private List<Long> extractIdList(Object obj) {
    if (!(obj instanceof List)) {
        return List.of();
    }

    List<?> rawList = (List<?>) obj;

    return rawList.stream()
            .filter(o -> o != null && (o instanceof Number || o instanceof String))
            .map(o -> (o instanceof Number)
                    ? ((Number) o).longValue()
                    : Long.parseLong(o.toString()))
            .toList();
}


	@Override
    @Transactional(readOnly = true)
    public ApiResponse getTeacherDocuments(Long teacherId) {
    	Teacher teacher = teacherRepository.findById(teacherId).orElse(null);
        User user = userRepository.findByUsername(teacher.getUsername())
            .orElseThrow(() -> new RuntimeException("User with ID " + teacherId + " not found"));

        List<Document> documents = documentRepository.findByUploadedBy(user);

        List<DocumentDTO> dtoList = documents.stream().map(doc -> {
            DocumentDTO dto = new DocumentDTO();
            dto.setId(doc.getId());
            dto.setName(doc.getName());
            dto.setFileType(doc.getContentType());
            dto.setFileSize(doc.getFileSize());
            dto.setAssociatedWith(doc.getAssociatedWith());
            dto.setDescription(doc.getDescription());
            dto.setUploadedBy(doc.getUploadedBy() != null ? doc.getUploadedBy().getUsername() : null);
            dto.setFileName(doc.getFileName());
            dto.setDownloadUrl("/api/admin/documents/" + doc.getId() + "/download");
            return dto;
        }).toList();

        return new ApiResponse("Documents fetched successfully", true, dtoList);
    }
    @Override
    public ApiResponse assignSubjectToTeacher(Long teacherId, Long subjectId) {
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Subject subject = subjectRepository.findById(subjectId)
            .orElseThrow(() -> new RuntimeException("Subject not found"));

        teacher.getSubjects().add(subject); // Assuming it's a Set<Subject>
        teacherRepository.save(teacher);

        return new ApiResponse("Subject assigned to teacher successfully", true);
    }

	@Override
	public ApiResponse toggleTeacherActiveStatus(Long teacherId) {
		 Teacher teacher = teacherRepository.findById(teacherId)
	                .orElseThrow(() -> new RuntimeException("Student not found"));
		 teacher.setActive(!teacher.isActive());
		 teacherRepository.save(teacher);
	        return new ApiResponse("Student active status updated", true, teacher.isActive());
	}

	@Override
	public ApiResponse getParentById(Long id) {
		Parent parent = parentRepository.findById(id).orElseThrow(() -> new RuntimeException("Parent not found with ID: " + id));
        return new ApiResponse("Parent fetched successfully", true, parent);
	}

    @Override
    public ApiResponse getAttendanceSummaryByClass(Long classId) {
        ClassRoom classRoom = classRoomRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));
        List<Student> students = studentRepository.findByClassRoomId(classId);
        List<Map<String, Object>> summary = students.stream().map(student -> {
            List<Attendance> records = attendanceRepository.findByStudent(student);
            long total = records.size();
            long present = records.stream()
                    .filter(a -> a.getStatus() == AttendanceStatus.PRESENT).count();
            Map<String, Object> entry = new HashMap<>();
            entry.put("studentId", student.getStudentId());
            entry.put("name", student.getFullName());
            entry.put("totalDays", total);
            entry.put("presentDays", present);
            entry.put("absentDays", total - present);
            entry.put("attendancePercentage",
                    total > 0 ? String.format("%.2f", (present * 100.0 / total)) + "%" : "0%");
            return entry;
        }).toList();
        Map<String, Object> result = new HashMap<>();
        result.put("class", classRoom.getClassName() + " - " + classRoom.getSection());
        result.put("students", summary);
        return new ApiResponse("Attendance summary fetched", true, result);
    }

    @Override
    public ApiResponse getFeeCollectionReport() {
        List<Student> students = studentRepository.findAll();
        List<Map<String, Object>> report = students.stream().map(student -> {
            double totalPaid = feePaymentRepository.findByStudentId(student.getId())
                    .stream().mapToDouble(p -> p.getAmountPaid()).sum();
            Map<String, Object> entry = new HashMap<>();
            entry.put("studentId", student.getStudentId());
            entry.put("name", student.getFullName());
            entry.put("class", student.getClassRoom() != null ? student.getClassRoom().getClassName() : "N/A");
            entry.put("totalPaid", totalPaid);
            return entry;
        }).toList();
        double grandTotal = report.stream()
                .mapToDouble(e -> (double) e.get("totalPaid")).sum();
        Map<String, Object> result = new HashMap<>();
        result.put("report", report);
        result.put("grandTotal", grandTotal);
        return new ApiResponse("Fee collection report fetched", true, result);
    }

    @Override
    public ApiResponse searchStudents(String name, Long classId, Boolean active) {
        List<Student> students;
        if (name != null && !name.isBlank()) {
            students = studentRepository.findByFullNameContainingIgnoreCase(name);
        } else if (classId != null) {
            students = studentRepository.findByClassRoomId(classId);
        } else if (active != null) {
            students = studentRepository.findByIsActive(active);
        } else {
            students = studentRepository.findAll();
        }
        return new ApiResponse("Search results", true, students);
    }

    @Override
    public ApiResponse getExamScheduleByClass(Long classId) {
        ClassRoom classRoom = classRoomRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));
        List<Exam> exams = examRepository.findByClassRoomIdOrderByExamDateAsc(classId);
        List<Map<String, Object>> schedule = exams.stream().map(exam -> {
            Map<String, Object> entry = new HashMap<>();
            entry.put("examId", exam.getId());
            entry.put("title", exam.getTitle());
            entry.put("subject", exam.getSubject() != null ? exam.getSubject().getName() : "N/A");
            entry.put("date", exam.getExamDate());
            entry.put("term", exam.getTerm());
            entry.put("academicYear", exam.getAcademicYear());
            return entry;
        }).toList();
        Map<String, Object> result = new HashMap<>();
        result.put("class", classRoom.getClassName() + " - " + classRoom.getSection());
        result.put("exams", schedule);
        return new ApiResponse("Exam schedule fetched", true, result);
    }

    @Override
    public ApiResponse getResultSummaryByTerm(String studentId, String term) {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        List<Result> results = resultRepository.findByStudentAndExam_Term(student, term);
        if (results.isEmpty())
            return new ApiResponse("No results found for term: " + term, false);
        double total = results.stream().mapToDouble(r -> r.getMarksObtained() != null ? r.getMarksObtained() : 0).sum();
        double average = total / results.size();
        long passed = results.stream().filter(r -> r.getMarksObtained() != null && r.getMarksObtained() >= 35).count();
        List<Map<String, Object>> details = results.stream().map(r -> {
            Map<String, Object> entry = new HashMap<>();
            entry.put("subject", r.getExam().getSubject() != null ? r.getExam().getSubject().getName() : "N/A");
            entry.put("examTitle", r.getExam().getTitle());
            entry.put("marks", r.getMarksObtained());
            entry.put("grade", r.getGrade());
            entry.put("status", r.getMarksObtained() != null && r.getMarksObtained() >= 35 ? "PASS" : "FAIL");
            return entry;
        }).toList();
        Map<String, Object> summary = new HashMap<>();
        summary.put("studentId", studentId);
        summary.put("name", student.getFullName());
        summary.put("term", term);
        summary.put("totalMarks", total);
        summary.put("average", String.format("%.2f", average));
        summary.put("passed", passed);
        summary.put("failed", results.size() - passed);
        summary.put("results", details);
        return new ApiResponse("Result summary fetched", true, summary);
    }

    @Override
    public ApiResponse searchTeachers(String name, String department) {
        List<Teacher> teachers;
        if (name != null && !name.isBlank()) {
            teachers = teacherRepository.findByFullNameContainingIgnoreCase(name);
        } else if (department != null && !department.isBlank()) {
            teachers = teacherRepository.findByDepartmentIgnoreCase(department);
        } else {
            teachers = teacherRepository.findAll();
        }
        List<TeacherResponseDTO> dtos = teachers.stream().map(this::mapToDTO).toList();
        return new ApiResponse("Teacher search results", true, dtos);
    }

    @Override
    public ApiResponse getClassWiseStudentCount() {
        List<ClassRoom> classes = classRoomRepository.findAll();
        List<Map<String, Object>> report = classes.stream().map(c -> {
            long count = studentRepository.findByClassRoomId(c.getId()).size();
            Map<String, Object> entry = new HashMap<>();
            entry.put("classId", c.getId());
            entry.put("className", c.getClassName() + " - " + c.getSection());
            entry.put("studentCount", count);
            return entry;
        }).toList();
        return new ApiResponse("Class-wise student count fetched", true, report);
    }

    @Override
    public ApiResponse getSubjectWiseResultAnalysis(Long classId) {
        ClassRoom classRoom = classRoomRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));
        List<Exam> exams = examRepository.findByClassRoom(classRoom);
        Map<String, List<Double>> subjectMarks = new HashMap<>();
        for (Exam exam : exams) {
            String subjectName = exam.getSubject() != null ? exam.getSubject().getName() : "Unknown";
            List<Result> results = resultRepository.findByExam(exam);
            results.stream()
                    .filter(r -> r.getMarksObtained() != null)
                    .forEach(r -> subjectMarks
                            .computeIfAbsent(subjectName, k -> new ArrayList<>())
                            .add(r.getMarksObtained()));
        }
        List<Map<String, Object>> analysis = subjectMarks.entrySet().stream().map(entry -> {
            List<Double> marks = entry.getValue();
            double avg = marks.stream().mapToDouble(Double::doubleValue).average().orElse(0);
            double highest = marks.stream().mapToDouble(Double::doubleValue).max().orElse(0);
            double lowest = marks.stream().mapToDouble(Double::doubleValue).min().orElse(0);
            long passed = marks.stream().filter(m -> m >= 35).count();
            Map<String, Object> row = new HashMap<>();
            row.put("subject", entry.getKey());
            row.put("average", String.format("%.2f", avg));
            row.put("highest", highest);
            row.put("lowest", lowest);
            row.put("totalStudents", marks.size());
            row.put("passed", passed);
            row.put("failed", marks.size() - passed);
            return row;
        }).toList();
        return new ApiResponse("Subject-wise result analysis fetched", true, analysis);
    }

    @Override
    public ApiResponse deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));
        studentRepository.delete(student);
        return new ApiResponse("Student deleted successfully", true);
    }

    @Override
    public ApiResponse deleteTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + id));
        teacherRepository.delete(teacher);
        return new ApiResponse("Teacher deleted successfully", true);
    }

    @Override
    public ApiResponse deleteParent(Long id) {
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parent not found with ID: " + id));
        parentRepository.delete(parent);
        return new ApiResponse("Parent deleted successfully", true);
    }

    @Override
    public ApiResponse deleteSubject(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with ID: " + id));
        subjectRepository.delete(subject);
        return new ApiResponse("Subject deleted successfully", true);
    }

    @Override
    public ApiResponse updateClass(Long classId, String className, String section) {
        ClassRoom classRoom = classRoomRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found with ID: " + classId));
        if (className != null && !className.isBlank()) classRoom.setClassName(className);
        if (section != null && !section.isBlank()) classRoom.setSection(section);
        classRoomRepository.save(classRoom);
        return new ApiResponse("Class updated successfully", true, classRoom);
    }

    @Override
    public ApiResponse getStudentProfile(String studentId) {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
        Map<String, Object> profile = new HashMap<>();
        profile.put("studentId", student.getStudentId());
        profile.put("name", student.getFullName());
        profile.put("email", student.getEmail());
        profile.put("phone", student.getPhone());
        profile.put("gender", student.getGender());
        profile.put("dob", student.getDateOfBirth());
        profile.put("class", student.getClassRoom() != null ? student.getClassRoom().getClassName() + " - " + student.getClassRoom().getSection() : "Not Assigned");
        profile.put("parent", student.getParent() != null ? student.getParent().getFullName() : "Not Linked");
        profile.put("active", student.isActive());
        return new ApiResponse("Student profile fetched", true, profile);
    }

    @Override
    public ApiResponse getEnhancedDashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("totalStudents", studentRepository.count());
        dashboard.put("totalTeachers", teacherRepository.count());
        dashboard.put("totalClasses", classRoomRepository.count());
        dashboard.put("totalSubjects", subjectRepository.count());
        dashboard.put("totalParents", parentRepository.count());
        dashboard.put("totalExams", examRepository.count());
        double totalFeesCollected = feePaymentRepository.findAll()
                .stream().mapToDouble(p -> p.getAmountPaid()).sum();
        dashboard.put("totalFeesCollected", totalFeesCollected);
        return new ApiResponse("Enhanced dashboard fetched", true, dashboard);
    }
}
