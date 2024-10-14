package com.beat.matrimonial.service;

import com.beat.matrimonial.dto.ProfileSearchDTO;
import com.beat.matrimonial.dto.SearchCriteria;
import com.beat.matrimonial.entity.Profile;
import com.beat.matrimonial.payload.response.MessageResponse;
import com.beat.matrimonial.payload.response.ProfileResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfileService {

  ProfileResponse getProfileByUserId(Long id);

  Profile updateProfile(Long userId, Profile profileDetails);

  MessageResponse updateProfileField(String fieldName, Object value);

  MessageResponse updateReligionAndSect(String religion, String sect);

  Page<ProfileSearchDTO> searchProfiles(SearchCriteria criteria, Pageable pageable);

  Profile getCurrentUserProfile();

}
