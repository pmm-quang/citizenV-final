package com.citizenv.app.payload.request;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class DivisionPopulationRequest {
    private String division;
    private Set<String> codes;
    private Set<String> properties;
}
