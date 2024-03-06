package com.inbank.decision.engine.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import static com.inbank.decision.engine.constant.Constants.*;

@Data
public class LoanApplicationDto {

    @NotNull(message = "Personal code is required")
    private String personalCode;

    @NotNull(message = "Loan amount is required")
    @Min(value = MIN_LOAN_AMOUNT, message = "Minimum loan amount is " + MIN_LOAN_AMOUNT)
    @Max(value = MAX_LOAN_AMOUNT, message = "Maximum loan amount is " + MAX_LOAN_AMOUNT)
    private Integer loanAmount;

    @NotNull(message = "Loan period is required")
    @Min(value = MIN_LOAN_PERIOD_MONTHS, message = "Minimum loan period is " + MIN_LOAN_PERIOD_MONTHS + " months")
    @Max(value = MAX_LOAN_PERIOD_MONTHS, message = "Maximum loan period is " + MAX_LOAN_PERIOD_MONTHS + " months")
    private Integer loanPeriod;

}
