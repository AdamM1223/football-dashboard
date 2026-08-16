package com.mccDev.football_dashboard.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Venue {
    private Integer id;
    private String name;
    private String address;
    private String city;
    private Integer capacity;
    private String surface;
    private String image;

}
