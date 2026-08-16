package com.mccDev.football_dashboard.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Time {
    private Integer elapsed;  // Minutes into the match
    private Integer extra;
}
