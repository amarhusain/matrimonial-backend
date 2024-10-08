package com.beat.matrimonial.service;

import com.beat.matrimonial.dto.ProfileSearchProjection;
import com.beat.matrimonial.repository.ProfileRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchService {


  private final ProfileRepository profileRepository;

  @Autowired
  public MatchService(ProfileRepository profileRepository) {
    this.profileRepository = profileRepository;
  }

  public List<ProfileSearchProjection> searchProfiles(String gender, String ageRange, String city) {
    // Implement search logic here. For simplicity, we are using a repository method.
    Integer ageStart = null;
    Integer ageEnd = null;

    // Parse age range
    if (ageRange != null && !ageRange.isEmpty()) {
      String[] parts = ageRange.split("-");
      ageStart = Integer.parseInt(parts[0]);
      ageEnd = Integer.parseInt(parts[1]);
    }
    return profileRepository.findByCriteria(gender, ageStart, ageEnd, city);
  }
}

