package com.mccDev.football_dashboard.models;

import lombok.Data;

@Data
public class PlayerRaw {
    private int id;
    private String name;
    private int age;
    private String nationality;
    private String position;
}
