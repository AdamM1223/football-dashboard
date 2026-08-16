package com.mccDev.football_dashboard.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class SquadContainer {
    @Schema(description = "Team Name", example = "Chelsea")
    private Team team;
    @Schema(description = "List of Players", example = "Chelsea Squad list..")
    private List<PlayerRaw> players;
}
