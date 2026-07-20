package com.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeeInvoiceDTO {
    private String category;
    private Double amount; 
    private Boolean paid;
    private String issuedDate; 
    private String paidDate;  
    private String studentName;
}
