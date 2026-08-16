package com.mccDev.football_dashboard.models;

import lombok.Data;

import java.util.List;

@Data
public class LeagueDetails {
    private List<List<StandingRaw>> standings;
}
