package com.mccDev.football_dashboard.models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Teams {
    private Team home;
    private Team away;

}
