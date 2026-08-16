package com.mccDev.football_dashboard.models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Team {
    private Integer id;
    private String name;
    private String code;
    private String country;
    private Integer founded;
    private Boolean national;
    private String logo;
//    private Boolean winner;

}
