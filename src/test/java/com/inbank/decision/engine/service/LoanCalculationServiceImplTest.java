package com.inbank.decision.engine.service;

import com.inbank.decision.engine.model.LoanApplicationStatus;
import com.inbank.decision.engine.service.impl.LoanCalculationServiceImpl;
import org.junit.jupiter.api.Test;

import static com.inbank.decision.engine.constant.Constants.MAX_LOAN_AMOUNT;
import static org.assertj.core.api.Assertions.assertThat;

class LoanCalculationServiceImplTest {

    private final LoanCalculationServiceImpl testObj = new LoanCalculationServiceImpl();

    @Test
    void testCalculateLoanOfferWithinLimits_Approved() {
        int creditModifier = 300;
        int requestedLoanAmount = 2000;
        int requestedLoanPeriod = 12;
        var result = testObj.calculateLoanOfferWithinLimits(creditModifier, requestedLoanAmount, requestedLoanPeriod);

        assertThat(result.getStatus()).isEqualTo(LoanApplicationStatus.APPROVED);
        assertThat(result.getAmount()).isGreaterThanOrEqualTo(2000);
    }

    @Test
    void testCalculateLoanOfferWithinLimits_NewOffer() {
        int creditModifier = 100;
        int requestedLoanAmount = 10000;
        int requestedLoanPeriod = 12;

        var result = testObj.calculateLoanOfferWithinLimits(creditModifier, requestedLoanAmount, requestedLoanPeriod);

        assertThat(result.getStatus()).isEqualTo(LoanApplicationStatus.NEW_OFFER);
        assertThat(result.getPeriod()).isGreaterThan(requestedLoanPeriod);
        assertThat(result.getAmount()).isLessThanOrEqualTo(MAX_LOAN_AMOUNT);
    }

}

