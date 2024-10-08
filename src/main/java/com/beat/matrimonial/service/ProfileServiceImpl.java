package com.beat.matrimonial.service;

import com.beat.matrimonial.dto.ProfileDto;
import com.beat.matrimonial.dto.UserDto;
import com.beat.matrimonial.entity.Profile;
import com.beat.matrimonial.entity.User;
import com.beat.matrimonial.exception.ResourceNotFoundException;
import com.beat.matrimonial.exception.UnderageException;
import com.beat.matrimonial.payload.response.MessageResponse;
import com.beat.matrimonial.payload.response.ProfileResponse;
import com.beat.matrimonial.repository.ProfileRepository;
import com.beat.matrimonial.repository.UserRepository;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfileServiceImpl implements ProfileService {

  private final ProfileRepository profileRepository;
  private final UserRepository userRepository;
  private final AuthService authService;

  private final String uploadDir = "./uploads/";

  private final Path fileStorageLocation;

  @Autowired
  public ProfileServiceImpl(
      ProfileRepository profileRepository,
      UserRepository userRepository,
      AuthService authService) {
    this.profileRepository = profileRepository;
    this.userRepository = userRepository;
    this.authService = authService;
    this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
  }

  @Override
  public ProfileResponse getProfileByUserId(Long userId) {
    // Fetch User entity by userId
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found for user ID: " + userId));

    // Fetch Profile entity by userId
    Profile profile = profileRepository.findByUserId(userId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Profile not found for user ID: " + userId));

    // Map User entity to UserDto
    UserDto userDto = getUserDto(user);

    // Map Profile entity to ProfileDto
    ProfileDto profileDto = getProfileDto(profile);

    // Calculate profile completion
    int profileCompletion = calculateProfileCompletion(user, profile);

    // Construct ProfileResponse and return
    ProfileResponse profileResponse = new ProfileResponse();
    profileResponse.setProfile(profileDto);
    profileResponse.setUser(userDto);
    profileResponse.setProfileCompletion(profileCompletion);  // Set profile completion percentage

    return profileResponse;
  }

  @Override
  @Transactional
  public Profile updateProfile(Long userId, Profile profileDetails) {
    Profile profile = profileRepository.findByUserId(userId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Profile not found for user ID: " + userId));
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

  @Override
  public MessageResponse updateProfileField(String fieldName, Object value) {
    Profile profile = getCurrentUserProfile();

    if (fieldName.equals("dateOfBirth")) {
      if (!isAtLeast21YearsOld(value.toString())) {
        throw new UnderageException("Person must be at least 21 years old.");
      }
      // Convert the date to ISO format (YYYY-MM-DD)
      value = LocalDate.parse(value.toString(), DateTimeFormatter.ISO_DATE);
    }

    try {
      Field field = Profile.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(profile, value);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException("Error updating field: " + fieldName, e);
    }

    Profile updatedProfile = profileRepository.save(profile);
    return new MessageResponse("Profile Updated sussessfully");
  }

  @Override
  public MessageResponse updateReligionAndSect(String religion, String sect) {
    Profile profile = getCurrentUserProfile();
    profile.setReligion(religion);
    profile.setSect(sect);
    Profile updatedProfile = profileRepository.save(profile);
    return new MessageResponse("Profile Updated sussessfully");
  }

  @Override
  public MessageResponse saveProfileImage(MultipartFile file) throws IOException {
    String fileName = file.getOriginalFilename();
    Path filePath = Paths.get(uploadDir + fileName);
    Files.createDirectories(filePath.getParent());
    Files.write(filePath, file.getBytes());

    Profile profile = getCurrentUserProfile();
    profile.setProfilePictureUrl(fileName);
    profileRepository.save(profile);
    return new MessageResponse("Profile picture uploaded successfully");
  }

  @Override
  public ResponseEntity<Resource> getProfileImageUrl() {
    User user = authService.getCurrentUser();
    Profile profile = profileRepository.findByUserId(user.getId())
        .orElseThrow(
            () -> new ResourceNotFoundException("Profile not found"));

    try {
      if (profile != null) {
        Path filePath = this.fileStorageLocation.resolve(profile.getProfilePictureUrl())
            .normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists()) {
          return ResponseEntity.ok()
              .header(HttpHeaders.CONTENT_DISPOSITION,
                  "attachment; filename=\"" + resource.getFilename() + "\"")
              .body(resource);
        }
      }
    } catch (MalformedURLException ex) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.notFound().build();
  }

  private Profile getCurrentUserProfile() {
    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    String username = userDetails.getUsername();
    User user = userRepository.findByEmail(username) // here username is email id of user
        .orElseThrow(() -> new RuntimeException("User not found"));
    return profileRepository.findByUserId(user.getId())
        .orElseThrow(() -> new RuntimeException("Profile not found for user"));
  }

  private ProfileDto getProfileDto(Profile profile) {
    return ProfileDto.builder()
        .id(profile.getId())
        .firstName(profile.getFirstName())
        .middleName(profile.getMiddleName())
        .lastName(profile.getLastName())
        .dateOfBirth(profile.getDateOfBirth())
        .gender(profile.getGender())
        .religion(profile.getReligion())
        .sect(profile.getSect())
        .occupation(profile.getOccupation())
        .address(profile.getAddress())
        .city(profile.getCity())
        .state(profile.getState())
        .country(profile.getCountry())
        .profilePictureUrl(profile.getProfilePictureUrl())
        .build();
  }

  private UserDto getUserDto(User user) {
    return UserDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .mobile(user.getMobile())
        .isEmailVerified(user.getIsEmailVerified())
        .isMobileVerified(user.getIsMobileVerified())
        .build();
  }

  private int calculateProfileCompletion(User user, Profile profile) {
    int totalFields = 10; // Total number of fields considered for completion
    int completedFields = 0;

    // Check User fields
    if (user.getEmail() != null && !user.getEmail().isEmpty()) {
      completedFields++;
    }
    if (user.getMobile() != null && !user.getMobile().isEmpty()) {
      completedFields++;
    }
    if (user.getIsEmailVerified() != null && user.getIsEmailVerified()) {
      completedFields++;
    }
    if (user.getIsMobileVerified() != null && user.getIsMobileVerified()) {
      completedFields++;
    }

    // Check Profile fields
    if (profile.getFirstName() != null && !profile.getFirstName().isEmpty()) {
      completedFields++;
    }
    if (profile.getLastName() != null && !profile.getLastName().isEmpty()) {
      completedFields++;
    }
    if (profile.getDateOfBirth() != null) {
      completedFields++;
    }
    if (profile.getGender() != null && !profile.getGender().isEmpty()) {
      completedFields++;
    }
    if (profile.getOccupation() != null && !profile.getOccupation().isEmpty()) {
      completedFields++;
    }

    // Calculate percentage
    int profileCompletionPercentage = (completedFields * 100) / totalFields;
    return profileCompletionPercentage;
  }

  public boolean isAtLeast21YearsOld(String dateOfBirth) {
    // Define the date format (assuming the input date is in "yyyy-MM-dd" format)
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Convert the string dateOfBirth into LocalDate
    LocalDate birthDate = LocalDate.parse(dateOfBirth, formatter);

    // Get the current date
    LocalDate currentDate = LocalDate.now();

    // Calculate the period between birthDate and currentDate
    Period age = Period.between(birthDate, currentDate);

    // Check if the person is at least 21 years old
    return age.getYears() >= 21;
  }

}
