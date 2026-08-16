package com.mccDev.football_dashboard.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class StandingRaw {
    @Schema(description = "Team's current rank", example = "1")
    private int rank;
    @Schema(description = "Team name", example = "Manchester City")
    private Team team;
    @Schema(description = "Total points", example = "89")
    private int points;

    @JsonProperty("goalsDifference")
    private int goalsDifference;
}
