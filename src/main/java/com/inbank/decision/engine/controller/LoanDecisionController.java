package com.inbank.decision.engine.controller;

import com.inbank.decision.engine.dto.LoanApplicationDto;
import com.inbank.decision.engine.dto.LoanApplicationResultDto;
import com.inbank.decision.engine.service.LoanDecisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loan")
@RequiredArgsConstructor
public class LoanDecisionController {

    private final LoanDecisionService loanDecisionService;

    @PostMapping("/decision")
    public ResponseEntity<LoanApplicationResultDto> makeLoanDecision(@RequestBody LoanApplicationDto application) {
        return ResponseEntity.ok(loanDecisionService.makeDecision(application));
    }
}
