package com.inbank.decision.engine.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inbank.decision.engine.model.LoanApplicationStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanApplicationResultDto {

    private LoanApplicationStatus status;
    private Integer amount;
    private Integer period;
}
