package com.inbank.decision.engine.dto;

import lombok.Data;

@Data
public class LoanApplicationDto {

    private String personalCode;
    private Integer loanAmount;
    private Integer loanPeriod;

}
