package com.mccDev.football_dashboard.models;

import lombok.Data;

import java.util.List;

@Data
public class StandingsWrapper {
    private List<LeagueContainer> response;
}
