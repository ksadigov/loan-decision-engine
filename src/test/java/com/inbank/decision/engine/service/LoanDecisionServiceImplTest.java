package com.inbank.decision.engine.service;

import com.inbank.decision.engine.client.RegistryApiClient;
import com.inbank.decision.engine.dto.LoanApplicationDto;
import com.inbank.decision.engine.dto.LoanApplicationResultDto;
import com.inbank.decision.engine.dto.LoanCalculationResultDto;
import com.inbank.decision.engine.model.BorrowerProfile;
import com.inbank.decision.engine.service.impl.LoanDecisionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class LoanDecisionServiceImplTest {

    @Mock
    private LoanCalculationService loanCalculationService;

    @Mock
    private RegistryApiClient registryApiClient;

    @InjectMocks
    private LoanDecisionServiceImpl testObj;

    @Test
    void makeDecision_shouldDenyLoanForNonexistentBorrowerProfile() {
        LoanApplicationDto application = new LoanApplicationDto();
        application.setPersonalCode("nonexistent");

        when(registryApiClient.getBorrowerProfile("nonexistent")).thenReturn(null);

        LoanApplicationResultDto result = testObj.makeDecision(application);

        assertThat(result.isApproved()).isFalse();
        assertThat(result.getMessage()).isEqualTo("Application Denied: The personal code provided does not exist in external registries.");
    }

    @Test
    void makeDecision_whenBorrowerHasOutstandingDebt_thenLoanApplicationIsDenied() {
        LoanApplicationDto application = new LoanApplicationDto();
        application.setPersonalCode("withDebt");

        BorrowerProfile profileWithDebt = new BorrowerProfile("12345678900", false, 0);

        when(registryApiClient.getBorrowerProfile("withDebt")).thenReturn(profileWithDebt);

        LoanApplicationResultDto result = testObj.makeDecision(application);

        assertThat(result.isApproved()).isFalse();
        assertThat(result.getMessage()).isEqualTo("Application Denied: Current outstanding debt detected.");
    }

    @Test
    void makeDecision_givenValidLoanApplication_thenLoanIsApproved() {
        LoanApplicationDto application = new LoanApplicationDto();
        application.setPersonalCode("validUser");
        application.setLoanAmount(5000);
        application.setLoanPeriod(12);

        BorrowerProfile validProfile = new BorrowerProfile("12345678999", false, 100);

        when(registryApiClient.getBorrowerProfile("validUser")).thenReturn(validProfile);

        LoanCalculationResultDto calculationResult = new LoanCalculationResultDto(true, 12, 5000);
        when(loanCalculationService.calculateLoanOfferWithinLimits(anyInt(), anyInt(), anyInt())).thenReturn(calculationResult);
        when(loanCalculationService.adjustLoanAmountToLimits(anyInt())).thenReturn(5000);
        when(loanCalculationService.calculatePotentialMaxLoanAmount(anyInt(), anyInt())).thenReturn(5000);

        LoanApplicationResultDto result = testObj.makeDecision(application);

        assertThat(result.isApproved()).isTrue();
        assertThat(result.getMessage()).isEqualTo("Congratulations! Your loan application is approved. We've found a loan option that matches your needs.");
    }

    @Test
    void makeDecision_shouldDenyLoanWhenRequestedAmountExceedsApprovalCriteria() {
        LoanApplicationDto application = new LoanApplicationDto();
        application.setPersonalCode("validButUnqualifiable");
        application.setLoanAmount(10000);
        application.setLoanPeriod(6);

        BorrowerProfile profileWithGoodCredit = new BorrowerProfile("98765432123", false, 100);

        when(registryApiClient.getBorrowerProfile("validButUnqualifiable")).thenReturn(profileWithGoodCredit);

        LoanCalculationResultDto calculationResult = new LoanCalculationResultDto(false, 6, 5000);
        when(loanCalculationService.calculateLoanOfferWithinLimits(anyInt(), anyInt(), anyInt())).thenReturn(calculationResult);

        LoanApplicationResultDto result = testObj.makeDecision(application);

        assertThat(result.isApproved()).isFalse();
        assertThat(result.getMessage()).isEqualTo("Currently, we are unable to offer a loan that meets your requirements and our lending criteria.");
    }

}
