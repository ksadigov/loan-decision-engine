package com.inbank.decision.engine.service.impl;

import com.inbank.decision.engine.dto.LoanApplicationDto;
import com.inbank.decision.engine.dto.LoanApplicationResultDto;
import com.inbank.decision.engine.dto.LoanCalculationResultDto;
import com.inbank.decision.engine.model.BorrowerProfile;
import com.inbank.decision.engine.service.LoanCalculationService;
import com.inbank.decision.engine.service.LoanDecisionService;
import com.inbank.decision.engine.util.MockRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanDecisionServiceImpl implements LoanDecisionService {

    private final LoanCalculationService loanCalculationService;

    @Override
    public LoanApplicationResultDto makeDecision(LoanApplicationDto application) {
        BorrowerProfile borrower = MockRegistry.getProfile(application.getPersonalCode());
        if (borrower == null) {
            return LoanApplicationResultDto.builder()
                    .approved(false)
                    .message("Application Denied: The personal code provided does not exist in external registries.").build();
        }
        if (borrower.getCreditModifier() == 0) {
            return LoanApplicationResultDto.builder()
                    .approved(false)
                    .message("Application Denied: Current outstanding debt detected.").build();
        }

        int requestedLoanPeriod = application.getLoanPeriod();
        int borrowerCreditModifier = borrower.getCreditModifier();
        int requestedLoanAmount = application.getLoanAmount();

        LoanCalculationResultDto calculationResult =
                loanCalculationService.calculateLoanOfferWithinLimits(borrowerCreditModifier, requestedLoanAmount, requestedLoanPeriod);
        if (!calculationResult.isLoanApproved()) {
            return LoanApplicationResultDto.builder()
                    .approved(false)
                    .message("Currently, we are unable to offer a loan that meets your requirements and our lending criteria.").build();
        }

        int finalMaxLoanAmount = loanCalculationService.adjustLoanAmountToLimits(calculationResult.getApprovedLoanAmount());
        return LoanApplicationResultDto.builder()
                .approved(true)
                .maxAmountForRequestedPeriod(loanCalculationService.calculatePotentialMaxLoanAmount(borrowerCreditModifier, requestedLoanPeriod))
                .approvedAmount(finalMaxLoanAmount)
                .approvedPeriod(calculationResult.getApprovedLoanPeriodMonths())
                .message("Congratulations! Your loan application is approved. We've found a loan option that matches your needs.")
                .application(application).build();
    }
}
