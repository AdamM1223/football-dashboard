package com.mccDev.football_dashboard.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RawMatch {
    private Fixture fixture;
    private Teams teams;
    private Goals goals;
}
