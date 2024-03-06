package com.inbank.decision.engine.service.impl;

import com.inbank.decision.engine.dto.LoanApplicationResultDto;
import com.inbank.decision.engine.model.LoanApplicationStatus;
import com.inbank.decision.engine.service.LoanCalculationService;
import org.springframework.stereotype.Service;

import static com.inbank.decision.engine.constant.Constants.*;

@Service
public class LoanCalculationServiceImpl implements LoanCalculationService {

    @Override
    public LoanApplicationResultDto calculateLoanOfferWithinLimits(int borrowerCreditModifier, int requestedLoanAmount, int requestedLoanPeriod) {
        int potentialMaxLoanAmount = calculatePotentialMaxLoanAmount(borrowerCreditModifier, requestedLoanPeriod);
        if (potentialMaxLoanAmount >= MIN_LOAN_AMOUNT) {
            return LoanApplicationResultDto.builder().status(LoanApplicationStatus.APPROVED).amount(potentialMaxLoanAmount).build();
        }

        return findAlternativeLoanOffer(requestedLoanPeriod, potentialMaxLoanAmount, requestedLoanAmount, borrowerCreditModifier);
    }

    private LoanApplicationResultDto findAlternativeLoanOffer(int requestedLoanPeriod, int potentialMaxLoanAmount, int requestedLoanAmount, int borrowerCreditModifier) {
        while (potentialMaxLoanAmount < requestedLoanAmount && requestedLoanPeriod < MAX_LOAN_PERIOD_MONTHS) {
            requestedLoanPeriod++;
            potentialMaxLoanAmount = calculatePotentialMaxLoanAmount(borrowerCreditModifier, requestedLoanPeriod);
        }

        return LoanApplicationResultDto.builder()
                .status(LoanApplicationStatus.NEW_OFFER)
                .amount(potentialMaxLoanAmount)
                .period(requestedLoanPeriod).build();
    }

    private int calculatePotentialMaxLoanAmount(int creditModifier, int loanPeriod) {
        return Math.min(creditModifier * loanPeriod, MAX_LOAN_AMOUNT);
    }
}
