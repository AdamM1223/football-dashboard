package com.mccDev.football_dashboard.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fixture {
//    private long id;
//    private String timezone;
    private String date;
//    private long timestamp;
//    private Periods periods;
//    private Venue venue;
    private Status status;
}
