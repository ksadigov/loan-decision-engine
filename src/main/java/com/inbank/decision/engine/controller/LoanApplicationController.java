package com.inbank.decision.engine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/loan-application")
public class LoanApplicationController {

    @GetMapping
    public String loanApplicationForm() {
        return "loanApplicationForm";
    }
}
