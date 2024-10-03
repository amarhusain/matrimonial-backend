package com.beat.matrimonial.controller;

import com.beat.matrimonial.entity.Profile;
import com.beat.matrimonial.service.ProfileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileControllerImpl implements ProfileController {

  private final ProfileServiceImpl profileService;

  @Autowired
  public ProfileControllerImpl(ProfileServiceImpl profileService) {
    this.profileService = profileService;
  }


  /**
   * Get User Profile by id
   *
   * @param userId the user id
   * @return profile
   */
  public ResponseEntity<Profile> getProfileByUserId(@PathVariable Long userId) {
    Profile profile = profileService.getProfileByUserId(userId);
    if (profile != null) {
      return ResponseEntity.ok(profile);
    } else {
      return ResponseEntity.notFound().build();
    }
  }


  /**
   * Update User Profile
   *
   * @param userId the user id
   * @return profile
   */
  public ResponseEntity<Profile> updateProfile(@PathVariable Long userId,
      @RequestBody Profile profileDetails) {
    Profile updatedProfile = profileService.updateProfile(userId, profileDetails);
    if (updatedProfile != null) {
      return ResponseEntity.ok(updatedProfile);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
