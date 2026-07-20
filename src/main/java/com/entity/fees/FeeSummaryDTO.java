package com.entity.fees;

import lombok.Data;

@Data
public class FeeSummaryDTO {
    private String feeType;
    private Double amountDue;
    private Double amountPaid;
    private boolean paid;
}
