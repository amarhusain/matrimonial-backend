package com.beat.matrimonial.service;

import com.beat.matrimonial.entity.Profile;

public interface ProfileService {

  Profile getProfileByUserId(Long id);
  
  Profile updateProfile(Long userId, Profile profileDetails);

}
