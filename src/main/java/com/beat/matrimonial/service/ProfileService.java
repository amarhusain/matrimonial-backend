package com.beat.matrimonial.service;

import com.beat.matrimonial.entity.Profile;
import com.beat.matrimonial.payload.response.MessageResponse;
import com.beat.matrimonial.payload.response.ProfileResponse;
import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {

  ProfileResponse getProfileByUserId(Long id);

  Profile updateProfile(Long userId, Profile profileDetails);

  MessageResponse updateProfileField(String fieldName, Object value);

  MessageResponse updateReligionAndSect(String religion, String sect);

  public MessageResponse saveProfileImage(MultipartFile file) throws IOException;

  public ResponseEntity<Resource> getProfileImageUrl();

}
