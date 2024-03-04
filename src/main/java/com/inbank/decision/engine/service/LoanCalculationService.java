package com.inbank.decision.engine.service;

import com.inbank.decision.engine.dto.LoanCalculationResultDto;

public interface LoanCalculationService {

    LoanCalculationResultDto calculateLoanOfferWithinLimits(int borrowerCreditModifier, int requestedLoanAmount, int requestedLoanPeriod);

    int adjustLoanAmountToLimits(int maxLoanAmount);

    int calculatePotentialMaxLoanAmount(int creditModifier, int loanPeriod);

}
