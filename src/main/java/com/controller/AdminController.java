package com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dto.ApiResponse;
import com.dto.CreateClassDTO;
import com.dto.RegisterParentDTO;
import com.dto.RegisterStudentDTO;
import com.dto.RegisterTeacherDTO;
import com.dto.SubjectDTO;
import com.entity.users.Document;
import com.repository.DocumentRepository;
import com.service.AdminService;

import lombok.RequiredArgsConstructor;

@Tag(name = "Admin", description = "Admin operations - manage students, teachers, parents, classes and subjects")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final DocumentRepository documentRepository;


    @Operation(summary = "Register student", description = "Register a new student and send welcome email")
    @PostMapping("/register-student")
    public ResponseEntity<ApiResponse> registerStudent(@Valid @RequestBody RegisterStudentDTO dto) {
       // System.out.println("Hello");
        return ResponseEntity.ok(adminService.registerStudent(dto));
    }

    @Operation(summary = "Register teacher", description = "Register a new teacher and send welcome email")
    @PostMapping("/register-teacher")
    public ResponseEntity<ApiResponse> registerTeacher(@Valid @RequestBody RegisterTeacherDTO dto) {
        return ResponseEntity.ok(adminService.registerTeacher(dto));
    }

    @Operation(summary = "Register parent", description = "Register a new parent and send welcome email")
    @PostMapping("/register-parent")
    public ResponseEntity<ApiResponse> registerParent(@Valid @RequestBody RegisterParentDTO dto) {
        return ResponseEntity.ok(adminService.registerParent(dto));
    }

    @PostMapping("/upload-document")
    public ResponseEntity<ApiResponse> uploadDocument(
            @RequestParam Long userId,
            @RequestParam String name,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(adminService.uploadDocument(userId, name, file));
    }

    @PostMapping("/create-class")
    public ResponseEntity<ApiResponse> createClass(@RequestBody CreateClassDTO dto) {
        return ResponseEntity.ok(adminService.createClass(dto.getClassName(), dto.getSection()));
    }

    @PostMapping("/assign-teacher")
    public ResponseEntity<ApiResponse> assignTeacherToClass(
            @RequestParam Long teacherId,
            @RequestParam Long classId
    ) {
        return ResponseEntity.ok(adminService.assignTeacherToClass(teacherId, classId));
    }

    @PostMapping("/assign-subject")
    public ResponseEntity<ApiResponse> assignSubjectToClass(
            @RequestParam Long subjectId,
            @RequestParam Long classId
    ) {
        return ResponseEntity.ok(adminService.assignSubjectToClass(subjectId, classId));
    }

    @PostMapping("/subject")
    public ResponseEntity<ApiResponse> createSubject(@RequestBody SubjectDTO dto) {
        return ResponseEntity.ok(adminService.createSubject(dto));
    }

    @Operation(summary = "Admin dashboard", description = "Get total counts of students, teachers and classes")
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse> getDashboardSummary() {
        return ResponseEntity.ok(adminService.getAdminDashboardSummary());
    }

    @Operation(summary = "Enhanced dashboard", description = "Get total students, teachers, classes, subjects, parents, exams and fees collected")
    @GetMapping("/dashboard/enhanced")
    public ResponseEntity<ApiResponse> getEnhancedDashboard() {
        return ResponseEntity.ok(adminService.getEnhancedDashboard());
    }

    @GetMapping("/students")
    public ResponseEntity<ApiResponse> getAllStudents() {
        return ResponseEntity.ok(adminService.getAllStudents());
    }

    @GetMapping("/teachers")
    public ResponseEntity<ApiResponse> getAllTeachers() {
        return ResponseEntity.ok(adminService.getAllTeachers());
    }

    @GetMapping("/classes")
    public ResponseEntity<ApiResponse> getAllClasses() {
        return ResponseEntity.ok(adminService.getAllClasses());
    }

    @GetMapping("/subjects")
    public ResponseEntity<ApiResponse> getAllSubjects() {
        return ResponseEntity.ok(adminService.getAllSubjects());
    }

    @GetMapping("/parents")
    public ResponseEntity<ApiResponse> getAllParents() {
        return ResponseEntity.ok(adminService.getAllParents());
    }

   
    @PutMapping("/students/{id}")
    public ResponseEntity<ApiResponse> updateStudent(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates
    ) {
        return ResponseEntity.ok(adminService.updateStudent(id, updates));
    }

    @PutMapping("/students/{id}/toggle-active")
    public ResponseEntity<ApiResponse> toggleStudentActive(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.toggleStudentActiveStatus(id));
    }

    @PutMapping("/students/{id}/assign-class")
    public ResponseEntity<ApiResponse> assignStudentClass(
            @PathVariable Long id,
            @RequestParam Long classId
    ) {
        return ResponseEntity.ok(adminService.assignStudentToClass(id, classId));
    }

    // ✅ New: Get individual student by ID
    @GetMapping("/students/{id}")
    public ResponseEntity<ApiResponse> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getStudentById(id));
    }

    @GetMapping("/students/{id}/documents")
    public ResponseEntity<ApiResponse> getStudentDocuments(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getStudentDocuments(id));
    }

    @GetMapping("/documents/{id}/download")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + doc.getFileName() + "\"")
                .header("Content-Type", doc.getContentType())
                .body(doc.getFileContent());
    }
    @DeleteMapping("/documents/{id}")
    public ResponseEntity<ApiResponse> deleteDocument(@PathVariable Long id) {
        documentRepository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse("Document deleted successfully", true));
    }
    @GetMapping("/teachers/{teacherId}")
    public ResponseEntity<ApiResponse> getTeacherByTeacherId(@PathVariable Long teacherId) {
        return ResponseEntity.ok(adminService.getTeacherByTeacherId(teacherId));
    }


    @PutMapping("/teachers/{id}")
    public ResponseEntity<ApiResponse> updateTeacher(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates
    ) {
        return ResponseEntity.ok(adminService.updateTeacher(id, updates));
    }

    @PutMapping("/teachers/{id}/toggle-active")
    public ResponseEntity<ApiResponse> toggleTeacherActive(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.toggleTeacherActiveStatus(id));
    }

   
    @PutMapping("/teachers/{id}/assign-class")
    public ResponseEntity<ApiResponse> assignTeacherClass(
            @PathVariable Long id,
            @RequestParam Long classId
    ) {
        return ResponseEntity.ok(adminService.assignTeacherToClass(id, classId));
    }

    @GetMapping("/teachers/{teacherId}/documents")
    public ResponseEntity<ApiResponse> getTeacherDocuments(@PathVariable Long teacherId) {
        return ResponseEntity.ok(adminService.getTeacherDocuments(teacherId));
    }
    @PostMapping("/teachers/{teacherId}/assign-subject")
    public ResponseEntity<ApiResponse> assignSubjectToTeacher(
            @PathVariable Long teacherId,
            @RequestParam Long subjectId
    ) {
        return ResponseEntity.ok(adminService.assignSubjectToTeacher(teacherId, subjectId));
    }
    @Operation(summary = "Get parent by ID")
    @GetMapping("/parents/{id}")
    public ResponseEntity<ApiResponse> getParentById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getParentById(id));
    }

    @Operation(summary = "Attendance summary by class", description = "Returns per-student attendance stats for a class")
    @GetMapping("/reports/attendance/{classId}")
    public ResponseEntity<ApiResponse> getAttendanceSummary(@PathVariable Long classId) {
        return ResponseEntity.ok(adminService.getAttendanceSummaryByClass(classId));
    }

    @Operation(summary = "Fee collection report", description = "Returns total fees paid per student across all classes")
    @GetMapping("/reports/fees")
    public ResponseEntity<ApiResponse> getFeeCollectionReport() {
        return ResponseEntity.ok(adminService.getFeeCollectionReport());
    }

    @Operation(summary = "Search students", description = "Search students by name, classId or active status")
    @GetMapping("/students/search")
    public ResponseEntity<ApiResponse> searchStudents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) Boolean active) {
        return ResponseEntity.ok(adminService.searchStudents(name, classId, active));
    }

    @Operation(summary = "Exam schedule by class", description = "Returns upcoming exams ordered by date for a class")
    @GetMapping("/reports/exams/{classId}")
    public ResponseEntity<ApiResponse> getExamSchedule(@PathVariable Long classId) {
        return ResponseEntity.ok(adminService.getExamScheduleByClass(classId));
    }

    @Operation(summary = "Result summary by term", description = "Returns total marks, average, pass/fail count for a student per term")
    @GetMapping("/reports/results/{studentId}")
    public ResponseEntity<ApiResponse> getResultSummary(
            @PathVariable String studentId,
            @RequestParam String term) {
        return ResponseEntity.ok(adminService.getResultSummaryByTerm(studentId, term));
    }

    @Operation(summary = "Search teachers", description = "Search teachers by name or department")
    @GetMapping("/teachers/search")
    public ResponseEntity<ApiResponse> searchTeachers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department) {
        return ResponseEntity.ok(adminService.searchTeachers(name, department));
    }

    @Operation(summary = "Class-wise student count", description = "Returns number of students in each class")
    @GetMapping("/reports/class-count")
    public ResponseEntity<ApiResponse> getClassWiseStudentCount() {
        return ResponseEntity.ok(adminService.getClassWiseStudentCount());
    }

    @Operation(summary = "Subject-wise result analysis", description = "Returns average, highest, lowest marks and pass/fail per subject for a class")
    @GetMapping("/reports/subject-analysis/{classId}")
    public ResponseEntity<ApiResponse> getSubjectWiseResultAnalysis(@PathVariable Long classId) {
        return ResponseEntity.ok(adminService.getSubjectWiseResultAnalysis(classId));
    }

    @Operation(summary = "Delete student")
    @DeleteMapping("/students/{id}")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.deleteStudent(id));
    }

    @Operation(summary = "Delete teacher")
    @DeleteMapping("/teachers/{id}")
    public ResponseEntity<ApiResponse> deleteTeacher(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.deleteTeacher(id));
    }

    @Operation(summary = "Delete parent")
    @DeleteMapping("/parents/{id}")
    public ResponseEntity<ApiResponse> deleteParent(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.deleteParent(id));
    }

    @Operation(summary = "Delete subject")
    @DeleteMapping("/subjects/{id}")
    public ResponseEntity<ApiResponse> deleteSubject(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.deleteSubject(id));
    }

    @Operation(summary = "Update class", description = "Update class name or section")
    @PutMapping("/classes/{classId}")
    public ResponseEntity<ApiResponse> updateClass(
            @PathVariable Long classId,
            @RequestParam(required = false) String className,
            @RequestParam(required = false) String section) {
        return ResponseEntity.ok(adminService.updateClass(classId, className, section));
    }

}
