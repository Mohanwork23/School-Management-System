package com.controller;

import com.entity.fees.FeeComponent;
import com.entity.fees.FeeStructure;
import com.entity.fees.FeePayment;
import com.service.FeeService;
import com.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminFeeController {

    private final FeeService feeService;

    @PostMapping("/fee-components")
    public ResponseEntity<ApiResponse> createComponent(@RequestBody FeeComponent component) {
        return ResponseEntity.ok(feeService.createComponent(component));
    }

    @GetMapping("/fee-components")
    public ResponseEntity<ApiResponse> getAllComponents() {
        return ResponseEntity.ok(feeService.getAllComponents());
    }

    @PostMapping("/fee-structure")
    public ResponseEntity<ApiResponse> setFeeStructure(@RequestBody FeeStructure structure) {
        return ResponseEntity.ok(feeService.setFeeStructure(structure));
    }

    @GetMapping("/fee-structure/{classId}")
    public ResponseEntity<ApiResponse> getFeeByClass(@PathVariable Long classId) {
        return ResponseEntity.ok(feeService.getFeeStructureByClass(classId));
    }

    @PostMapping("/fee-payments")
    public ResponseEntity<ApiResponse> addPayment(@RequestBody FeePayment payment) {
        return ResponseEntity.ok(feeService.addPayment(payment));
    }

    @GetMapping("/fee-payments/student/{studentId}")
    public ResponseEntity<ApiResponse> getPaymentsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(feeService.getPaymentsByStudent(studentId));
    }
}
