package com.service.implement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dto.ApiResponse;
import com.dto.AttendanceDTO;
import com.dto.ResultDTO;
import com.entity.fees.FeePayment;
import com.entity.users.Parent;
import com.entity.users.Student;
import com.exception.ResourceNotFoundException;
import com.repository.AttendanceRepository;
import com.repository.ParentRepository;
import com.repository.ResultRepository;
import com.repository.StudentRepository;
import com.repository.fees.FeePaymentRepository;
import com.service.ParentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {

    private final ParentRepository parentRepository;
    private final ResultRepository resultRepository;
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final FeePaymentRepository feePaymentRepository;

    private Parent getParent(String parentId) {
        return parentRepository.findByParentId(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent not found with ID: " + parentId));
    }

    private List<Student> getChildrenOfParent(Parent parent) {
        List<Student> children = studentRepository.findByParent(parent);
        if (children.isEmpty()) {
            throw new ResourceNotFoundException("No students linked to this parent.");
        }
        return children;
    }

    @Override
    public ApiResponse getChildGrades(String parentId) {
        Parent parent = getParent(parentId);
        List<Student> children = getChildrenOfParent(parent);

        List<ResultDTO> allResults = children.stream()
                .flatMap(student -> resultRepository.findByStudent(student).stream()
                        .map(result -> {
                            ResultDTO dto = new ResultDTO();
                            dto.setExamTitle(result.getExam().getTitle());
                            dto.setSubjectName(result.getExam().getSubject().getName());
                            dto.setMarksObtained(result.getMarksObtained());
                            dto.setTerm(result.getExam().getTerm());
                            dto.setAcademicYear(result.getExam().getAcademicYear());
                            dto.setStudentName(student.getFullName());
                            return dto;
                        }))
                .collect(Collectors.toList());

        return new ApiResponse("Child grades fetched successfully.", true, allResults);
    }

    @Override
    public ApiResponse getChildAttendance(String parentId) {
        Parent parent = getParent(parentId);
        List<Student> children = getChildrenOfParent(parent);

        List<AttendanceDTO> allAttendance = children.stream()
                .flatMap(student -> attendanceRepository.findByStudent(student).stream()
                        .map(a -> {
                            AttendanceDTO dto = new AttendanceDTO();
                            dto.setDate(a.getDate().toString());
                            dto.setStatus(a.getStatus());
                            dto.setRemarks(a.getRemarks());
                            dto.setStudentName(student.getFullName());
                            return dto;
                        }))
                .collect(Collectors.toList());

        return new ApiResponse("Child attendance fetched successfully.", true, allAttendance);
    }

    @Override
    public ApiResponse getChildFeeStatus(String parentId) {
        Parent parent = getParent(parentId);
        List<Student> children = getChildrenOfParent(parent);
        List<Map<String, Object>> feeList = children.stream().map(child -> {
            List<FeePayment> payments = feePaymentRepository.findByStudentId(child.getId());
            double totalPaid = payments.stream().mapToDouble(FeePayment::getAmountPaid).sum();
            Map<String, Object> entry = new HashMap<>();
            entry.put("studentName", child.getFullName());
            entry.put("studentId", child.getStudentId());
            entry.put("totalPaid", totalPaid);
            entry.put("payments", payments);
            return entry;
        }).collect(Collectors.toList());
        return new ApiResponse("Child fee status fetched", true, feeList);
    }

    @Override
    public ApiResponse getChildResults(String parentId) {
        Parent parent = getParent(parentId);
        List<Student> children = getChildrenOfParent(parent);

        List<ResultDTO> resultList = children.stream()
                .flatMap(student -> resultRepository.findByStudent(student).stream()
                        .map(result -> {
                            ResultDTO dto = new ResultDTO();
                            dto.setExamTitle(result.getExam().getTitle());
                            dto.setSubjectName(result.getExam().getSubject().getName());
                            dto.setMarksObtained(result.getMarksObtained());
                            dto.setTerm(result.getExam().getTerm());
                            dto.setAcademicYear(result.getExam().getAcademicYear());
                            dto.setStudentName(student.getFullName());
                            return dto;
                        }))
                .collect(Collectors.toList());

        return new ApiResponse("Child results fetched successfully.", true, resultList);
    }

    @Override
    public ApiResponse getParentDashboard(String parentId) {
        Parent parent = getParent(parentId);
        List<Student> children = getChildrenOfParent(parent);

        List<Map<String, Object>> childSummaries = children.stream().map(child -> {
            Map<String, Object> summary = new HashMap<>();
            summary.put("studentId", child.getStudentId());
            summary.put("name", child.getFullName());
            summary.put("class", child.getClassRoom() != null ? child.getClassRoom().getClassName() : "Not Assigned");
            summary.put("dob", child.getDateOfBirth());
            summary.put("gender", child.getGender());
            return summary;
        }).collect(Collectors.toList());

        return new ApiResponse("Parent dashboard fetched successfully.", true, childSummaries);
    }

    @Override
    public ApiResponse getParentProfile(String parentId) {
        Parent parent = getParent(parentId);
        Map<String, Object> profile = new HashMap<>();
        profile.put("parentId", parent.getParentId());
        profile.put("name", parent.getFullName());
        profile.put("email", parent.getEmail());
        profile.put("phone", parent.getPhone());
        profile.put("address", parent.getAddress());
        profile.put("gender", parent.getGender());
        profile.put("role", parent.getRole().name());

        return new ApiResponse("Parent profile fetched successfully.", true, profile);
    }
}
