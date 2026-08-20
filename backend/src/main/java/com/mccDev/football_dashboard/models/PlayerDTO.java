package com.mccDev.football_dashboard.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
public class PlayerDTO {

    @Schema(description = "Player ID", example = "23")
    private Integer id;
    @Schema(description = "Player name", example = "Enzo Fernandez")
    private String name;
    @Schema(description = "Player Age", example = "24")
    private int age;
    @Schema(description = "Player position", example = "Midfielder")
    private String position;
    @Schema(description = "Player nationality", example = "Argentinian")
    private String nationality;

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
}
