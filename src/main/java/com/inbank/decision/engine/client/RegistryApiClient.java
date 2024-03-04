package com.inbank.decision.engine.client;

import com.inbank.decision.engine.model.BorrowerProfile;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class RegistryApiClient {

    private static final Map<String, BorrowerProfile> PROFILES = Map.of(
            "49002010965", new BorrowerProfile("49002010965", true, 0),
            "49002010976", new BorrowerProfile("49002010976", false, 100),
            "49002010987", new BorrowerProfile("49002010987", false, 300),
            "49002010998", new BorrowerProfile("49002010998", false, 1000));


    public BorrowerProfile getBorrowerProfile(String personalCode) {
        return PROFILES.getOrDefault(personalCode, new BorrowerProfile(personalCode, false, 0));
    }
}
