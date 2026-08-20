package com.mccDev.football_dashboard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mccDev.football_dashboard.exception.ApiException;
import com.mccDev.football_dashboard.models.*;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FootballService {

    @Value("${api.sports.key}")
    private String apiKey;

    private final MeterRegistry meterRegistry;
    private final RestTemplate restTemplate = new RestTemplate();

    public FootballService(MeterRegistry meterRegistry){
        this.meterRegistry = meterRegistry;
    }

    public List<MatchScore> getLiveScores() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-apisports-key", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "https://v3.football.api-sports.io/fixtures?live=all",
                HttpMethod.GET,
                entity,
                String.class
        );

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        JsonNode fixturesArray = root.path("response");

        if (response.getBody() == null || response.getBody().isEmpty()) {
            throw new ApiException("No live fixtures found", HttpStatus.NOT_FOUND);
        }

        List<RawMatch> rawMatches = mapper.readValue(
                fixturesArray.toString(),
                new TypeReference<List<RawMatch>>() {}
        );

        return rawMatches.stream().map(match -> {
            MatchScore score = new MatchScore();
            score.setHomeTeam(match.getTeams().getHome().getName());
            score.setAwayTeam(match.getTeams().getAway().getName());
            score.setHomeGoals(match.getGoals().getHome());
            score.setAwayGoals(match.getGoals().getAway());
            score.setStatus(match.getFixture().getStatus().getLongStatus());
            return score;
        }).collect(Collectors.toList());
    }

    @Cacheable(value = "teams", key = "#leagueId + '-' + #season")
    public List<Team> getAllTeams(Integer leagueId, Integer season) {
        int finalLeagueId = (leagueId != null) ? leagueId : 39;
        int finalSeason = (season != null) ? season : 2025;

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-apisports-key", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://v3.football.api-sports.io/teams?league=" + finalLeagueId + "&season=" + finalSeason;

        ResponseEntity<LeagueResponseWrapper> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, LeagueResponseWrapper.class
        );

        if (response.getBody() == null || response.getBody().getResponse() == null || response.getBody().getResponse().isEmpty()) {
            throw new ApiException("No teams found for this league and season", HttpStatus.NOT_FOUND);
        }

        return response.getBody().getResponse().stream()
                .map(TeamWrapper::getTeam)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "standings", key = "#leagueId + '-' + #season")
    public List<StandingRaw> getStandings(Integer leagueId, Integer season) {
        int finalLeagueId = (leagueId != null) ? leagueId : 39;
        int finalSeasonId = (season != null) ? season : 2025;
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-apisports-key", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = "https://v3.football.api-sports.io/standings?league=" + finalLeagueId + "&season=" + finalSeasonId;

        ResponseEntity<StandingsWrapper> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, StandingsWrapper.class
        );
        if (response.getBody() == null || response.getBody().getResponse().isEmpty()) {
            throw new ApiException("No standings found for this league and season", HttpStatus.NOT_FOUND);
        }
        return response.getBody().getResponse().get(0).getLeague().getStandings().get(0);
    }

    @Cacheable(value = "squads", key = "#teamId + '-' + #season")
    public List<PlayerDTO> getSquad(Integer teamId, String season) {
        int finalTeamId = (teamId != null) ? teamId : 49;
        String finalSeason = (season != null && !season.trim().isEmpty()) ? season : "2025";

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-apisports-key", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        List<PlayerDTO> dtos = new ArrayList<>();

        // CURRENT LIVE SEASON: /players/squads (Real-time roster)
        if ("2025".equals(finalSeason)) {
            String url = "https://v3.football.api-sports.io/players/squads?team=" + finalTeamId;

            ResponseEntity<SquadWrapper> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, SquadWrapper.class
            );

            if (response.getBody() != null && response.getBody().getResponse() != null && !response.getBody().getResponse().isEmpty()) {
                List<PlayerRaw> rawPlayers = response.getBody().getResponse().get(0).getPlayers();
                dtos = rawPlayers.stream().map(p -> {
                    PlayerDTO dto = new PlayerDTO();
                    dto.setId(p.getId());
                    dto.setName(p.getName());
                    dto.setAge(p.getAge());
                    dto.setPosition(p.getPosition());
                    dto.setNationality(p.getNationality());
                    return dto;
                }).collect(Collectors.toList());
            }

            // HISTORICAL SEASONS: /players?team=X&season=Y with Pagination handling
        } else {
            int currentPage = 1;
            int totalPages = 1;

            do {
                String url = String.format("https://v3.football.api-sports.io/players?team=%d&season=%s&page=%d", finalTeamId, finalSeason, currentPage);

                ResponseEntity<JsonNode> response = restTemplate.exchange(
                        url, HttpMethod.GET, entity, JsonNode.class
                );

                if (response.getBody() != null && response.getBody().has("response")) {
                    totalPages = response.getBody().path("paging").path("total").asInt(1);
                    JsonNode responseArray = response.getBody().get("response");

                    for (JsonNode node : responseArray) {
                        JsonNode playerNode = node.get("player");
                        JsonNode statsNode = node.get("statistics").get(0);

                        PlayerDTO dto = new PlayerDTO();
                        dto.setId(playerNode.get("id").asInt());
                        dto.setName(playerNode.get("name").asText());
                        dto.setAge(playerNode.hasNonNull("age") ? playerNode.get("age").asInt() : 0);
                        dto.setNationality(playerNode.hasNonNull("nationality") ? playerNode.get("nationality").asText() : "Unknown");
                        dto.setPosition(statsNode.path("games").path("position").asText("N/A"));
                        dtos.add(dto);
                    }
                }
                currentPage++;
            } while (currentPage <= totalPages);
        }

        if (dtos.isEmpty()) {
            throw new ApiException("No squad data found for team ID " + finalTeamId + " in season " + finalSeason, HttpStatus.NOT_FOUND);
        }

        return dtos;
    }

    @Cacheable(value = "playerDetails", key = "#playerId + '-' + #season")
    public PlayerDetailDTO getPlayerDetail(Integer playerId, String season) {
        String finalSeason = (season != null && !season.trim().isEmpty()) ? season : "2025";
        String url = String.format("https://v3.football.api-sports.io/players?id=%d&season=%s", playerId, finalSeason);

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-apisports-key", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, JsonNode.class
        );

        if (response.getBody() == null || !response.getBody().has("response") || response.getBody().get("response").isEmpty()) {
            return null;
        }

        JsonNode root = response.getBody().get("response").get(0);
        JsonNode playerNode = root.get("player");
        JsonNode statsNode = root.get("statistics").get(0);

        PlayerDetailDTO detail = new PlayerDetailDTO();
        detail.setId(playerNode.get("id").asInt());
        detail.setName(playerNode.get("name").asText());
        detail.setAge(playerNode.hasNonNull("age") ? playerNode.get("age").asInt() : 0);
        detail.setNationality(playerNode.hasNonNull("nationality") ? playerNode.get("nationality").asText() : "Unknown");
        detail.setPhoto(playerNode.hasNonNull("photo") ? playerNode.get("photo").asText() : null);
        detail.setPosition(statsNode.path("games").path("position").asText("N/A"));

        PlayerDetailDTO.PlayerStats stats = new PlayerDetailDTO.PlayerStats();
        // FIXED TYPO: "appearances" instead of "appearences"
        stats.setAppearances(statsNode.path("games").path("appearances").asInt(0));
        stats.setRating(statsNode.path("games").path("rating").asText("N/A"));
        stats.setGoals(statsNode.path("goals").path("total").asInt(0));
        stats.setAssists(statsNode.path("goals").path("assists").asInt(0));
        stats.setYellowCards(statsNode.path("cards").path("yellow").asInt(0));

        detail.setStats(stats);
        return detail;
    }
}