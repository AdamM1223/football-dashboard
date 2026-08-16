package com.mccDev.football_dashboard.models;

import lombok.Data;

import java.util.List;

@Data
public class SquadWrapper {
    private List<SquadContainer> response;
}
