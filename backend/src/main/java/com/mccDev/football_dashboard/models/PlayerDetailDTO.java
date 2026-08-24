package com.mccDev.football_dashboard.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class PlayerDetailDTO extends PlayerDTO {

    @Schema(description = "Player ID", example = "23")
    private Integer id;

    @Schema(description = "Player Photo URL", example = "https://media.api-sports.io/football/players/23.png")
    private String photo;

    private PlayerStats stats;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public PlayerStats getStats() { return stats; }
    public void setStats(PlayerStats stats) { this.stats = stats; }

    public static class PlayerStats{
        private int appearances;
        private int goals;
        private int assists;
        private int yellowCards;
        private String rating;

        public int getAppearances() { return appearances; }
        public void setAppearances(int appearances) { this.appearances = appearances; }

        public int getGoals() { return goals; }
        public void setGoals(int goals) { this.goals = goals; }

        public int getAssists() { return assists; }
        public void setAssists(int assists) { this.assists = assists; }

        public int getYellowCards() { return yellowCards; }
        public void setYellowCards(int yellowCards) { this.yellowCards = yellowCards; }

        public String getRating() { return rating; }
        public void setRating(String rating) { this.rating = rating; }
    }
}