package com.mccDev.football_dashboard.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mccDev.football_dashboard.models.*;
import com.mccDev.football_dashboard.service.FootballService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@RestController
@RequestMapping("/api/football")
public class FootballController {

    @Autowired
    private FootballService footballService;

    @Tag(name = "Scores", description = "Scores")
    @Operation(summary = "Get Live Score", description = "Returns Scores List")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval"),
            @ApiResponse(responseCode = "404", description = "Results not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/scores")
    public ResponseEntity<List<MatchScore>> getScores() throws JsonProcessingException {
        return ResponseEntity.ok(footballService.getLiveScores());
    }

    @Tag(name = "Teams", description = "Teams")
    @Operation(summary = "Get Teams from a specific league & season", description = "Returns Teams List for a given league and season")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval"),
            @ApiResponse(responseCode = "404", description = "Team not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    // Defaults to English League
    @GetMapping("/teams")
    public ResponseEntity<List<Team>> getAllTeams(
            @RequestParam(name = "league", required = false, defaultValue = "39") Integer leagueId,
            @RequestParam(name = "season", required = false, defaultValue = "2023") Integer season) {

        return ResponseEntity.ok(footballService.getAllTeams(leagueId, season));
        }

    @Tag(name = "Standings", description = "League standings operations")
    @Operation(summary = "Get league standings", description = "Returns standings for a given league and season")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval"),
            @ApiResponse(responseCode = "404", description = "Standings not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/standings")
    public ResponseEntity<List<StandingRaw>> getStandings(
            @RequestParam(name = "league", defaultValue = "39") Integer leagueId,
            @RequestParam(name = "season", defaultValue = "2023") Integer season) {
        List<StandingRaw> standings = footballService.getStandings(leagueId, season);
        return ResponseEntity.ok(standings);
    }

    @Tag(name = "Players", description = "Team List Operations")
    @Operation(summary = "Get squad list", description = "Returns the squad for a given team")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Squad retrieved"),
            @ApiResponse(responseCode = "404", description = "Players not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/players")
    public ResponseEntity<List<PlayerDTO>> getSquad(    @Parameter(description = "Team ID (defaults to Chelsea)", example = "49") @RequestParam(required = false) Integer teamId)  {
        return ResponseEntity.ok(footballService.getSquad(teamId));
    }
}
