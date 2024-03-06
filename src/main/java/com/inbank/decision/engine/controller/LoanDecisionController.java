package com.inbank.decision.engine.controller;

import com.inbank.decision.engine.dto.LoanApplicationDto;
import com.inbank.decision.engine.dto.LoanApplicationResultDto;
import com.inbank.decision.engine.service.LoanDecisionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
public class LoanDecisionController {

    private final LoanDecisionService loanDecisionService;

    @PostMapping("/decision")
    public ResponseEntity<LoanApplicationResultDto> makeLoanDecision(@Valid @RequestBody LoanApplicationDto application) {
        return new ResponseEntity<>(loanDecisionService.makeDecision(application), HttpStatus.OK);
    }
}
