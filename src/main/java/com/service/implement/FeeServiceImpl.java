package com.service.implement;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dto.ApiResponse;
import com.entity.academic.ClassRoom;
import com.entity.fees.FeeComponent;
import com.entity.fees.FeePayment;
import com.entity.fees.FeeStructure;
import com.entity.users.Student;
import com.repository.ClassRoomRepository;
import com.repository.StudentRepository;
import com.repository.fees.FeeComponentRepository;
import com.repository.fees.FeePaymentRepository;
import com.repository.fees.FeeStructureRepository;
import com.service.FeeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeeServiceImpl implements FeeService {

    private final FeeComponentRepository feeComponentRepository;
    private final FeeStructureRepository feeStructureRepository;
    private final FeePaymentRepository feePaymentRepository;
    private final ClassRoomRepository classRoomRepository;
    private final StudentRepository studentRepository;

    @Override
    public ApiResponse createComponent(FeeComponent component) {
        feeComponentRepository.save(component);
        return new ApiResponse("Fee component created", true, component);
    }

    @Override
    public ApiResponse getAllComponents() {
        return new ApiResponse("All fee components", true, feeComponentRepository.findAll());
    }

    @Override
    public ApiResponse setFeeStructure(FeeStructure structure) {
        Long classId = structure.getClassRoom().getId();
        Long componentId = structure.getComponent().getId();

        ClassRoom classRoom = classRoomRepository.findById(classId)
            .orElseThrow(() -> new RuntimeException("Class not found"));
        FeeComponent component = feeComponentRepository.findById(componentId)
            .orElseThrow(() -> new RuntimeException("Component not found"));

        structure.setClassRoom(classRoom);
        structure.setComponent(component);
        feeStructureRepository.save(structure);
        return new ApiResponse("Fee structure saved", true, structure);
    }

    @Override
    public ApiResponse getFeeStructureByClass(Long classId) {
        List<FeeStructure> list = feeStructureRepository.findByClassRoomId(classId);
        return new ApiResponse("Fee structure for class " + classId, true, list);
    }

    @Override
    public ApiResponse addPayment(FeePayment payment) {
        Student student = studentRepository.findById(payment.getStudent().getId())
            .orElseThrow(() -> new RuntimeException("Student not found"));

        FeeStructure structure = feeStructureRepository.findById(payment.getFeeStructure().getId())
            .orElseThrow(() -> new RuntimeException("Fee structure not found"));

        payment.setStudent(student);
        payment.setFeeStructure(structure);
        feePaymentRepository.save(payment);

        return new ApiResponse("Payment added", true, payment);
    }

    @Override
    public ApiResponse getPaymentsByStudent(Long studentId) {
        List<FeePayment> payments = feePaymentRepository.findByStudentId(studentId);
        return new ApiResponse("Payments for student", true, payments);
    }
}
