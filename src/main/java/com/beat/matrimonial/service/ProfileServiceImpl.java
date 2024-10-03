package com.beat.matrimonial.service;

import com.beat.matrimonial.entity.Profile;
import com.beat.matrimonial.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileServiceImpl implements ProfileService {

  private final ProfileRepository profileRepository;

  @Autowired
  public ProfileServiceImpl(ProfileRepository profileRepository) {
    this.profileRepository = profileRepository;
  }

  @Override
  public Profile getProfileByUserId(Long userId) {
    return profileRepository.findByUserId(userId);
  }

  @Override
  @Transactional
  public Profile updateProfile(Long userId, Profile profileDetails) {
    Profile profile = profileRepository.findByUserId(userId);
    if (profile != null) {
      // Update fields
      profile.setFirstName(profileDetails.getFirstName());
      profile.setMiddleName(profileDetails.getMiddleName());
      profile.setLastName(profileDetails.getLastName());
      profile.setDateOfBirth(profileDetails.getDateOfBirth());
      profile.setGender(profileDetails.getGender());
      profile.setReligion(profileDetails.getReligion());
      profile.setOccupation(profileDetails.getOccupation());
      profile.setAddress(profileDetails.getAddress());
      profile.setCity(profileDetails.getCity());
      profile.setState(profileDetails.getState());
      profile.setCountry(profileDetails.getCountry());
      profile.setProfilePictureUrl(profileDetails.getProfilePictureUrl());
      profile.setBio(profileDetails.getBio());

      return profileRepository.save(profile);
    }
    return null;
  }

}
