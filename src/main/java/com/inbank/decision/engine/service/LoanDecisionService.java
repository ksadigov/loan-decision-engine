package com.inbank.decision.engine.service;

import com.inbank.decision.engine.dto.LoanApplicationDto;
import com.inbank.decision.engine.dto.LoanApplicationResultDto;

public interface LoanDecisionService {
    LoanApplicationResultDto makeDecision(LoanApplicationDto application);
}
