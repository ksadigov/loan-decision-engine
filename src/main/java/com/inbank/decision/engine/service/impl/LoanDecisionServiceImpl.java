package com.inbank.decision.engine.service.impl;

import com.inbank.decision.engine.client.RegistryApiClient;
import com.inbank.decision.engine.dto.LoanApplicationDto;
import com.inbank.decision.engine.dto.LoanApplicationResultDto;
import com.inbank.decision.engine.exception.BorrowerNotFoundException;
import com.inbank.decision.engine.model.LoanApplicationStatus;
import com.inbank.decision.engine.service.LoanCalculationService;
import com.inbank.decision.engine.service.LoanDecisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanDecisionServiceImpl implements LoanDecisionService {

    private final LoanCalculationService loanCalculationService;
    private final RegistryApiClient registryApiClient;

    @Override
    public LoanApplicationResultDto makeDecision(LoanApplicationDto application) {
        var borrower = registryApiClient.getBorrowerProfile(application.getPersonalCode());
        if (borrower == null) {
            throw new BorrowerNotFoundException("Borrower with personal code " + application.getPersonalCode() + " not found.");
        }

        if (borrower.getCreditModifier() == 0) {
            return LoanApplicationResultDto.builder()
                    .status(LoanApplicationStatus.NOT_APPROVED).build();
        }

        return loanCalculationService.calculateLoanOfferWithinLimits(borrower.getCreditModifier(),
                application.getLoanAmount(), application.getLoanPeriod());
    }

}
