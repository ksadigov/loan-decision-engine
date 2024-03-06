package com.inbank.decision.engine.service;

import com.inbank.decision.engine.dto.LoanApplicationResultDto;

public interface LoanCalculationService {

    LoanApplicationResultDto calculateLoanOfferWithinLimits(int borrowerCreditModifier, int requestedLoanAmount, int requestedLoanPeriod);

}
