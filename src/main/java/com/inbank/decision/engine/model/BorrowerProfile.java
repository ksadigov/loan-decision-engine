package com.inbank.decision.engine.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BorrowerProfile {
    private String personalCode;
    private int creditModifier;
}
