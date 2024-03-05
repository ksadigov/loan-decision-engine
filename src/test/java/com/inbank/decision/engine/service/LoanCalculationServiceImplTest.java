package com.inbank.decision.engine.service;

import com.inbank.decision.engine.service.impl.LoanCalculationServiceImpl;
import org.junit.jupiter.api.Test;

import static com.inbank.decision.engine.constant.Constants.MAX_LOAN_AMOUNT;
import static org.assertj.core.api.Assertions.assertThat;

class LoanCalculationServiceImplTest {

    private final LoanCalculationServiceImpl testObj = new LoanCalculationServiceImpl();

    @Test
    void testCalculateLoanOfferWithinLimits_Approved() {
        int creditModifier = 100;
        int requestedLoanAmount = 2000;
        int requestedLoanPeriod = 12;
        var result = testObj.calculateLoanOfferWithinLimits(creditModifier, requestedLoanAmount, requestedLoanPeriod);

        assertThat(result.isLoanApproved()).isTrue();
        assertThat(result.getApprovedLoanPeriodMonths()).isEqualTo(20);
        assertThat(result.getApprovedLoanAmount()).isEqualTo(2000);
    }

    @Test
    void testCalculateLoanOfferWithinLimits_NotApproved() {
        int creditModifier = 10;
        int requestedLoanAmount = 3000;
        int requestedLoanPeriod = 12;
        var result = testObj.calculateLoanOfferWithinLimits(creditModifier, requestedLoanAmount, requestedLoanPeriod);

        assertThat(result.isLoanApproved()).isFalse();
    }

    @Test
    void adjustLoanAmountToLimits_whenLoanAmountWithinLimits_thenReturnsSameAmount() {
        int loanAmount = 5000;
        assertThat(testObj.adjustLoanAmountToLimits(loanAmount)).isEqualTo(loanAmount);
    }

    @Test
    void adjustLoanAmountToLimits_whenLoanAmountBelowMinimum_thenAdjustsToZero() {
        int loanAmountBelowMinimum = 200;
        assertThat(testObj.adjustLoanAmountToLimits(loanAmountBelowMinimum)).isZero();
    }

    @Test
    void adjustLoanAmountToLimits_whenLoanAmountAboveMaximum_thenAdjustsToMaximumLoanAmount() {
        int loanAmountAboveMaximum = MAX_LOAN_AMOUNT + 1000;
        assertThat(testObj.adjustLoanAmountToLimits(loanAmountAboveMaximum)).isEqualTo(MAX_LOAN_AMOUNT);
    }

}

