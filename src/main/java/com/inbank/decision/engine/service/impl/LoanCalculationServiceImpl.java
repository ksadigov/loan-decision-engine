package com.inbank.decision.engine.service.impl;

import com.inbank.decision.engine.dto.LoanCalculationResultDto;
import com.inbank.decision.engine.service.LoanCalculationService;
import org.springframework.stereotype.Service;

import static com.inbank.decision.engine.constant.Constants.*;

@Service
public class LoanCalculationServiceImpl implements LoanCalculationService {

    @Override
    public LoanCalculationResultDto calculateLoanOfferWithinLimits(int borrowerCreditModifier, int requestedLoanAmount, int requestedLoanPeriod) {
        int potentialMaxLoanAmount = calculatePotentialMaxLoanAmount(borrowerCreditModifier, requestedLoanPeriod);
        int loanPeriod = requestedLoanPeriod;
        while (potentialMaxLoanAmount < requestedLoanAmount) {
            if (loanPeriod >= MAX_LOAN_PERIOD_MONTHS) {
                return new LoanCalculationResultDto(false, loanPeriod, potentialMaxLoanAmount);
            }
            loanPeriod++;
            potentialMaxLoanAmount = borrowerCreditModifier * loanPeriod;
        }
        return new LoanCalculationResultDto(true, loanPeriod, potentialMaxLoanAmount);
    }

    @Override
    public int adjustLoanAmountToLimits(int maxLoanAmount) {
        return maxLoanAmount < MIN_LOAN_AMOUNT ? 0 : Math.min(MAX_LOAN_AMOUNT, maxLoanAmount);
    }

    @Override
    public int calculatePotentialMaxLoanAmount(int creditModifier, int loanPeriod) {
        return creditModifier * loanPeriod;
    }
}
