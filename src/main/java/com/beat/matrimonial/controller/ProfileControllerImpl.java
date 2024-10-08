package com.beat.matrimonial.controller;

import com.beat.matrimonial.entity.Profile;
import com.beat.matrimonial.payload.response.MessageResponse;
import com.beat.matrimonial.payload.response.ProfileResponse;
import com.beat.matrimonial.service.ProfileServiceImpl;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
  @GetMapping("/{userId}")
  public ResponseEntity<ProfileResponse> getProfileByUserId(@PathVariable Long userId) {
    ProfileResponse profileResponse = profileService.getProfileByUserId(userId);
    return ResponseEntity.ok(profileResponse);
  }


  /**
   * Update User Profile
   *
   * @param userId the user id
   * @return profile
   */
  @PutMapping("/{userId}")
  public ResponseEntity<Profile> updateProfile(@PathVariable Long userId,
      @RequestBody Profile profileDetails) {
    Profile updatedProfile = profileService.updateProfile(userId, profileDetails);
    if (updatedProfile != null) {
      return ResponseEntity.ok(updatedProfile);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @Override
  @PatchMapping("/update-profile-field")
  public ResponseEntity<MessageResponse> updateProfileField(
      @RequestBody Map<String, Object> updates) {
    String field = updates.keySet().iterator().next(); // Get the first (and only) key
    Object value = updates.get(field);
    MessageResponse messageResponse = profileService.updateProfileField(field, value);
    return ResponseEntity.ok(messageResponse);
  }

  @Override
  @PutMapping("/religion")
  public ResponseEntity<MessageResponse> updateReligionAndSect(@RequestParam String religion,
      @RequestParam(required = false) String sect) {
    MessageResponse messageResponse = profileService.updateReligionAndSect(religion, sect);
    return ResponseEntity.ok(messageResponse);
  }

  @Override
  @PostMapping("/image")
  public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
    try {
      profileService.saveProfileImage(file);
      return ResponseEntity.ok().build();
    } catch (IOException e) {
      return ResponseEntity.internalServerError().body("Failed to upload image");
    }
  }

  @Override
  @GetMapping("/image")
  public ResponseEntity<Resource> getProfileImage() {
    return profileService.getProfileImageUrl();
  }

}
