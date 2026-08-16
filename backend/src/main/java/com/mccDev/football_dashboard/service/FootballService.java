package com.mccDev.football_dashboard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mccDev.football_dashboard.exception.ApiException;
import com.mccDev.football_dashboard.models.*;
import io.micrometer.core.instrument.Counter;
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

        System.out.println("Raw API Response: " + response.getBody());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        JsonNode fixturesArray = root.path("response");
        List<RawMatch> rawMatches = mapper.readValue(
                fixturesArray.toString(),
                new TypeReference<List<RawMatch>>() {}
        );

        if (response.getBody() == null || response.getBody().isEmpty()) {
            throw new ApiException("No standings found for this league and season", HttpStatus.NOT_FOUND);
        }
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

    public List<Team> getAllTeams(Integer leagueId, Integer season) {
        int finalLeagueId = (leagueId != null) ? leagueId : 39;
        int finalSeason = (season != null) ? season : 2023;

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-apisports-key", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://v3.football.api-sports.io/teams?league=" + finalLeagueId + "&season=" + finalSeason;

        ResponseEntity<LeagueResponseWrapper> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                LeagueResponseWrapper.class
        );
        System.out.println("Raw response body: " + response.getBody());

        if (response.getBody() == null || response.getBody().getResponse() == null || response.getBody().getResponse().isEmpty()) {
            throw new ApiException("No teams found for this league and season", HttpStatus.NOT_FOUND);
        }

        return response.getBody().getResponse().stream()
                .map(TeamWrapper::getTeam)
                .collect(Collectors.toList());

    }

    public List<StandingRaw> getStandings(Integer leagueId, Integer season) {
            int finalLeagueId = (leagueId != null) ? leagueId : 39;
            int finalSeasonId = (season != null) ? season : 2023;
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-apisports-key", apiKey);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            String url = "https://v3.football.api-sports.io/standings?league=" + finalLeagueId + "&season=" + finalSeasonId;
            ResponseEntity<StandingsWrapper> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    StandingsWrapper.class
            );
            if (response.getBody() == null || response.getBody().getResponse().isEmpty()) {
                throw new ApiException("No standings found for this league and season", HttpStatus.NOT_FOUND);
            }
            return response.getBody().getResponse().get(0).getLeague().getStandings().get(0);
        }

        public List<PlayerDTO> getSquad(Integer teamId) {
            int finalTeamId = (teamId != null) ? teamId : 49; // Defaults this value to Chelsea

            try {
                HttpHeaders headers = new HttpHeaders();
                headers.set("x-apisports-key", apiKey);
                HttpEntity<String> entity = new HttpEntity<>(headers);
                String url = "https://v3.football.api-sports.io/players/squads?team=" + finalTeamId;
                ResponseEntity<SquadWrapper> response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        SquadWrapper.class
                );

                if (response.getBody() == null || response.getBody().getResponse().isEmpty()) {
                    throw new ApiException("No standings found for this league and season", HttpStatus.NOT_FOUND);
                }

                List<PlayerRaw> rawPlayers = response.getBody().getResponse().get(0).getPlayers();

                List<PlayerDTO> dtos = rawPlayers.stream().map(p -> {
                    PlayerDTO dto = new PlayerDTO();
                    dto.setName(p.getName());
                    dto.setAge(p.getAge());
                    dto.setPosition(p.getPosition());
                    dto.setNationality(p.getNationality());
                    return dto;
                }).collect(Collectors.toList());

                Counter.builder("football_squad_fetches_total")
                        .description("Total number of squad data fetches")
                        .tag("team_id", String.valueOf(finalTeamId))
                        .tag("status", "success")
                        .register(meterRegistry)
                        .increment();

                return dtos;
            } catch (Exception e) {
                Counter.builder("football_squad_fetches_total")
                        .description("Total number of squad data fetches")
                        .tag("team_id", String.valueOf(finalTeamId))
                        .tag("status", "error")
                        .register(meterRegistry)
                        .increment();

                throw e;
            }
        }
}
