package com.mccDev.football_dashboard.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Represents a team's current standing in the league")
public class LeagueStanding {
    @Schema(description = "Team's current rank", example = "1")
    private int rank;
    @Schema(description = "Team name", example = "Manchester City")
    private String team;
    @Schema(description = "Total points", example = "89")
    private int points;
    @Schema(description = "Goal difference", example = "45")
    private int goalDifference;
}
