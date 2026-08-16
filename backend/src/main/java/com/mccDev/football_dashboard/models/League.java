package com.mccDev.football_dashboard.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class League {
    private Integer id;
    private String name;
    private String country;
    private Integer season;
    private String round;
    private boolean standings;

}
