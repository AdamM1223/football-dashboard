package com.mccDev.football_dashboard.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchScore {
    @Schema(description = "Home Team name", example = "Manchester City")
    private String homeTeam;
    @Schema(description = "Away Team name", example = "Manchester United")
    private String awayTeam;
    @Schema(description = "Home Team Goals", example = "1")
    private Integer homeGoals;
    @Schema(description = "Away Team Goals", example = "0")
    private Integer awayGoals;
    @Schema(description = "Match status ", example = "Finished")
    private String status;
}
