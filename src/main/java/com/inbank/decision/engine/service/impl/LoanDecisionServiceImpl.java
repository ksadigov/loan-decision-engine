package com.inbank.decision.engine.service.impl;

import com.inbank.decision.engine.client.RegistryApiClient;
import com.inbank.decision.engine.dto.LoanApplicationDto;
import com.inbank.decision.engine.dto.LoanApplicationResultDto;
import com.inbank.decision.engine.dto.LoanCalculationResultDto;
import com.inbank.decision.engine.model.BorrowerProfile;
import com.inbank.decision.engine.service.LoanCalculationService;
import com.inbank.decision.engine.service.LoanDecisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.inbank.decision.engine.constant.Constants.MAX_LOAN_AMOUNT;

@Service
@RequiredArgsConstructor
public class LoanDecisionServiceImpl implements LoanDecisionService {

    private final LoanCalculationService loanCalculationService;
    private final RegistryApiClient registryApiClient;

    @Override
    public LoanApplicationResultDto makeDecision(LoanApplicationDto application) {
        BorrowerProfile borrower = registryApiClient.getBorrowerProfile(application.getPersonalCode());
        if (borrower == null) {
            return applicationDenied("Application Denied: The personal code provided does not exist in external registries.");
        }

        if (borrower.getCreditModifier() == 0) {
            return applicationDenied("Application Denied: Current outstanding debt detected.");
        }

        LoanCalculationResultDto calculationResult = calculateLoanOffer(application, borrower);
        if (!calculationResult.isLoanApproved()) {
            return applicationDenied("Currently, we are unable to offer a loan that meets your requirements and our lending criteria.");
        }

        return prepareApprovalResponse(calculationResult, borrower.getCreditModifier(), application.getLoanPeriod());
    }

    private LoanApplicationResultDto applicationDenied(String message) {
        return LoanApplicationResultDto.builder()
                .approved(false)
                .message(message)
                .build();
    }

    private LoanCalculationResultDto calculateLoanOffer(LoanApplicationDto application, BorrowerProfile borrower) {
        return loanCalculationService.calculateLoanOfferWithinLimits(borrower.getCreditModifier(),
                application.getLoanAmount(), application.getLoanPeriod());
    }

    private LoanApplicationResultDto prepareApprovalResponse(LoanCalculationResultDto calculationResult, int borrowerCreditModifier, int requestedLoanPeriod) {
        int finalMaxLoanAmount = loanCalculationService.adjustLoanAmountToLimits(calculationResult.getApprovedLoanAmount());
        int potentialMaxLoanAmount = calculateAdjustedPotentialMaxLoanAmount(borrowerCreditModifier, requestedLoanPeriod);

        return LoanApplicationResultDto.builder()
                .approved(true)
                .maxAmountForRequestedPeriod(potentialMaxLoanAmount)
                .approvedAmount(finalMaxLoanAmount)
                .approvedPeriod(calculationResult.getApprovedLoanPeriodMonths())
                .message("Congratulations! Your loan application is approved. We've found a loan option that matches your needs.")
                .build();
    }

    private int calculateAdjustedPotentialMaxLoanAmount(int borrowerCreditModifier, int requestedLoanPeriod) {
        int potentialMaxLoanAmount = loanCalculationService.calculatePotentialMaxLoanAmount(borrowerCreditModifier, requestedLoanPeriod);
        return Math.min(potentialMaxLoanAmount, MAX_LOAN_AMOUNT);
    }

}
