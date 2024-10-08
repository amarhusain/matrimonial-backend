package com.beat.matrimonial.controller;

import com.beat.matrimonial.dto.ProfileSearchProjection;
import com.beat.matrimonial.service.MatchService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

  private final MatchService matchService;

  @Autowired
  public MatchController(MatchService matchService) {
    this.matchService = matchService;
  }

  // Search for profiles based on criteria
  @GetMapping("/search")
  public ResponseEntity<List<ProfileSearchProjection>> searchMatches(
      @RequestParam(required = false) String gender,
      @RequestParam(required = false) String ageRange,
      @RequestParam(required = false) String city) {

    List<ProfileSearchProjection> matchedProfiles = matchService.searchProfiles(gender, ageRange,
        city);
    return new ResponseEntity<>(matchedProfiles, HttpStatus.OK);
  }
}

