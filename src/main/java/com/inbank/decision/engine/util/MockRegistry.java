package com.inbank.decision.engine.util;

import com.inbank.decision.engine.model.BorrowerProfile;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MockRegistry {
    private static final Map<String, BorrowerProfile> profiles = Map.of(
            "49002010965", new BorrowerProfile("49002010965", true, 0), // Person with debt
            "49002010976", new BorrowerProfile("49002010976", false, 100), // Segment 1
            "49002010987", new BorrowerProfile("49002010987", false, 300), // Segment 2
            "49002010998", new BorrowerProfile("49002010998", false, 1000) // Segment 3
    );


    public static BorrowerProfile getProfile(String personalCode) {
        return profiles.getOrDefault(personalCode, new BorrowerProfile(personalCode, false, 0));
    }
}
