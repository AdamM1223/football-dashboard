package com.mccDev.football_dashboard.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PlayerDTO {
    @Schema(description = "Player name", example = "Enzo Fernandez")
    private String name;
    @Schema(description = "Player Age", example = "24")
    private int age;
    @Schema(description = "Player position", example = "Midfielder")
    private String position;
    @Schema(description = "Player nationality", example = "Argentinian")
    private String nationality;
}
