package com.service;

import com.entity.fees.FeeComponent;
import com.entity.fees.FeeStructure;
import com.entity.fees.FeePayment;
import com.dto.ApiResponse;

public interface FeeService {

    ApiResponse createComponent(FeeComponent component);
    ApiResponse getAllComponents();

    ApiResponse setFeeStructure(FeeStructure structure);
    ApiResponse getFeeStructureByClass(Long classId);

    ApiResponse addPayment(FeePayment payment);
    ApiResponse getPaymentsByStudent(Long studentId);
}
