package com.inbank.decision.engine.service;

import com.inbank.decision.engine.client.RegistryApiClient;
import com.inbank.decision.engine.dto.LoanApplicationDto;
import com.inbank.decision.engine.dto.LoanApplicationResultDto;
import com.inbank.decision.engine.exception.BorrowerNotFoundException;
import com.inbank.decision.engine.model.BorrowerProfile;
import com.inbank.decision.engine.model.LoanApplicationStatus;
import com.inbank.decision.engine.service.impl.LoanDecisionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LoanDecisionServiceImplTest {

    @Mock
    private LoanCalculationService loanCalculationService;

    @Mock
    private RegistryApiClient registryApiClient;

    @InjectMocks
    private LoanDecisionServiceImpl testObj;

    @Test
    void makeDecision_borrowerNotFound_throwsException() {
        LoanApplicationDto application = new LoanApplicationDto();
        application.setPersonalCode("nonexistent");

        when(registryApiClient.getBorrowerProfile("nonexistent")).thenReturn(null);

        assertThrows(BorrowerNotFoundException.class, () -> testObj.makeDecision(application));

        verify(registryApiClient).getBorrowerProfile("nonexistent");
        verifyNoInteractions(loanCalculationService);
    }

    @Test
    void makeDecision_borrowerCreditModifierIsZero_returnsNotApproved() {
        LoanApplicationDto application = new LoanApplicationDto();
        application.setPersonalCode("123");
        BorrowerProfile borrower = new BorrowerProfile("123", 0);

        when(registryApiClient.getBorrowerProfile("123")).thenReturn(borrower);

        LoanApplicationResultDto result = testObj.makeDecision(application);

        assertThat(result.getStatus()).isEqualTo(LoanApplicationStatus.NOT_APPROVED);
        verify(registryApiClient).getBorrowerProfile("123");
        verifyNoInteractions(loanCalculationService);
    }

    @Test
    void makeDecision_validBorrower_callsLoanCalculationService() {
        LoanApplicationDto application = new LoanApplicationDto();
        application.setPersonalCode("123");
        application.setLoanAmount(5000);
        application.setLoanPeriod(24);
        BorrowerProfile borrower = new BorrowerProfile("123", 10);

        when(registryApiClient.getBorrowerProfile("123")).thenReturn(borrower);
        when(loanCalculationService.calculateLoanOfferWithinLimits(anyInt(), anyInt(), anyInt()))
                .thenReturn(LoanApplicationResultDto.builder().status(LoanApplicationStatus.APPROVED).amount(5000).period(12).build());

        LoanApplicationResultDto result = testObj.makeDecision(application);

        assertThat(result.getStatus()).isEqualTo(LoanApplicationStatus.APPROVED);
        assertThat(result.getAmount()).isEqualTo(5000);
        assertThat(result.getPeriod()).isEqualTo(12);
        verify(registryApiClient).getBorrowerProfile("123");
        verify(loanCalculationService).calculateLoanOfferWithinLimits(borrower.getCreditModifier(), application.getLoanAmount(), application.getLoanPeriod());
    }
}
