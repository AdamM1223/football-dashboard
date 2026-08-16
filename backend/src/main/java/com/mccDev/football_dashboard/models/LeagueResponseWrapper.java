package com.mccDev.football_dashboard.models;

import lombok.Data;

import java.util.List;

@Data
public class LeagueResponseWrapper {
    private List<TeamWrapper> response;
}
