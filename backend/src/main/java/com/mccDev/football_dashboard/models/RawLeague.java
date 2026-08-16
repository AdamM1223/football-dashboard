package com.mccDev.football_dashboard.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RawLeague {
    @Schema(description = "League ID", example = "49")
    private Integer id;
    @Schema(description = "League Name", example = "La liga")
    private League league;
    @Schema(description = "League Country", example = "Spain")
    private String country;
}
