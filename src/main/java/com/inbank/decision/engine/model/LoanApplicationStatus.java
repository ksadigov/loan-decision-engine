package com.inbank.decision.engine.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum LoanApplicationStatus {
    APPROVED("Congratulations! Your loan application is approved."),
    NOT_APPROVED("Not approved. Current outstanding debt detected."),
    IN_PROGRESS("Your application is in progress."),
    NEW_OFFER("We have a new offer for you.");

    private final String message;
}
