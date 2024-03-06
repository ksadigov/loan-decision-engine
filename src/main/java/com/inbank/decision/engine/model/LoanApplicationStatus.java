package com.inbank.decision.engine.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum LoanApplicationStatus {
    APPROVED,
    NOT_APPROVED,
    IN_PROGRESS,
    NEW_OFFER;
}
