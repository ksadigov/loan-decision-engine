package com.inbank.decision.engine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoanCalculationResultDto {
    private boolean isLoanApproved;
    private int approvedLoanPeriodMonths;
    private int approvedLoanAmount;
}
