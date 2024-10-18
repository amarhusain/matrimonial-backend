package com.beat.matrimonial.controller;

import com.beat.matrimonial.dto.ProfileSearchDTO;
import com.beat.matrimonial.dto.SearchCriteria;
import com.beat.matrimonial.entity.Profile;
import com.beat.matrimonial.payload.response.MessageResponse;
import com.beat.matrimonial.payload.response.ProfileResponse;
import com.beat.matrimonial.service.ProfileServiceImpl;
import com.beat.matrimonial.service.SubscriptionService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileControllerImpl implements ProfileController {

  private final ProfileServiceImpl profileService;
  private final SubscriptionService subscriptionService;

  @Autowired
  public ProfileControllerImpl(ProfileServiceImpl profileService,
      SubscriptionService subscriptionService) {
    this.profileService = profileService;
    this.subscriptionService = subscriptionService;
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
  @GetMapping("/search")
  public ResponseEntity<Page<ProfileSearchDTO>> searchProfiles(
      @RequestParam(required = false) String lookingFor,
      @RequestParam(required = false) Integer minAge,
      @RequestParam(required = false) Integer maxAge,
      @RequestParam(required = false) Integer minHeight,
      @RequestParam(required = false) Integer maxHeight,
      @RequestParam(required = false) String religion,
      @RequestParam(required = false) String sect,
      @RequestParam(required = false) Integer minIncome,
      @RequestParam(required = false) Integer maxIncome,
      @RequestParam(required = false) String maritalStatus,
      @RequestParam(required = false) String profilePhoto,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size) {

    SearchCriteria criteria = new SearchCriteria(lookingFor, minAge, maxAge, minHeight, maxHeight,
        religion, sect, minIncome, maxIncome, maritalStatus, profilePhoto);
    Pageable pageable = PageRequest.of(page, size);
    Page<ProfileSearchDTO> list = profileService.searchProfiles(criteria, pageable);
    return ResponseEntity.ok(list);
  }


  @GetMapping("/{id}/subscription-status")
  public ResponseEntity<Boolean> checkSubscriptionStatus(@PathVariable Long id) {
    boolean hasSubscription = subscriptionService.hasActiveSubscription(id);
    return ResponseEntity.ok(hasSubscription);
  }

}
