package com.mccDev.football_dashboard.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Status {
    private String longStatus;
//    @JsonProperty("short")
//    private String shortStatus;
//    private Integer elapsed;
//    private String extra;

}
