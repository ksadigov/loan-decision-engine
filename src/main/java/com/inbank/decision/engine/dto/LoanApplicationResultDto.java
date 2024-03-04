package com.inbank.decision.engine.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanApplicationResultDto {

    private boolean approved;
    private int maxAmountForRequestedPeriod;
    private int approvedAmount;
    private int approvedPeriod;
    private String message;
}
