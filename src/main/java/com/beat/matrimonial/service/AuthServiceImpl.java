package com.beat.matrimonial.service;

import com.beat.matrimonial.entity.Profile;
import com.beat.matrimonial.entity.Role;
import com.beat.matrimonial.entity.User;
import com.beat.matrimonial.enums.ERole;
import com.beat.matrimonial.exception.InvalidOtpException;
import com.beat.matrimonial.exception.RoleNotFoundException;
import com.beat.matrimonial.exception.SignupRequestNotFoundException;
import com.beat.matrimonial.exception.UnauthorizedException;
import com.beat.matrimonial.exception.UserAlreadyExistsException;
import com.beat.matrimonial.payload.request.LoginRequest;
import com.beat.matrimonial.payload.request.OtpValidationRequest;
import com.beat.matrimonial.payload.request.SignupRequest;
import com.beat.matrimonial.payload.response.JwtResponse;
import com.beat.matrimonial.payload.response.MessageResponse;
import com.beat.matrimonial.repository.ProfileRepository;
import com.beat.matrimonial.repository.RoleRepository;
import com.beat.matrimonial.repository.UserRepository;
import com.beat.matrimonial.security.jwt.JwtUtils;
import com.beat.matrimonial.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthServiceImpl implements AuthService {

  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final ProfileRepository profileRepository;
  private final PasswordEncoder encoder;
  private final JwtUtils jwtUtils;

  private final Map<String, SignupRequest> pendingSignups = new HashMap<>();
  private final Map<String, String> otpStorage = new HashMap<>();

  public AuthServiceImpl(AuthenticationManager authenticationManager,
      UserRepository userRepository,
      RoleRepository roleRepository,
      ProfileRepository profileRepository,
      PasswordEncoder passwordEncoder,
      JwtUtils jwtUtils) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.profileRepository = profileRepository;
    this.encoder = passwordEncoder;
    this.jwtUtils = jwtUtils;
  }

  @Override
  public JwtResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              loginRequest.getUsername(),
              loginRequest.getPassword()
          )
      );

      SecurityContextHolder.getContext().setAuthentication(authentication);
      String jwt = jwtUtils.generateJwtToken(authentication);

      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
      List<String> roles = userDetails.getAuthorities().stream()
          .map(item -> item.getAuthority())
          .collect(Collectors.toList());

      return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles);

    } catch (BadCredentialsException e) {
      throw new UnauthorizedException("Invalid credentials");
    }
  }


  @Override
  public String initiateSignup(SignupRequest signupRequest) {
    String contactInfo = signupRequest.getEmailOrMobile();

    // Validate if email or mobile already exists
    if (userRepository.existsByEmailOrMobile(contactInfo)) {
      if (isEmail(contactInfo)) {
        throw new UserAlreadyExistsException("User already exists with this email");
      } else {
        throw new UserAlreadyExistsException("User already exists with this mobile");
      }
    }

    String signupId = UUID.randomUUID().toString();

    // Generate and store OTP
    String otp = generateOtp();
    otpStorage.put(signupId, otp);

    // Store signup request
    pendingSignups.put(signupId, signupRequest);

    // Send OTP
    if (isEmail(contactInfo)) {
      sendEmailOtp(contactInfo, otp);
    } else {
      sendMobileOtp(contactInfo, otp);
    }
    return signupId;
  }

  @Override
  @Transactional
  public MessageResponse validateOtpAndCompleteSignup(
      OtpValidationRequest otpValidationRequest) {
    String storedOtp = otpStorage.get(otpValidationRequest.getSignupId());
    if (storedOtp == null || !storedOtp.equals(otpValidationRequest.getOtp())) {
      throw new InvalidOtpException("Invalid OTP");
    }
    SignupRequest signupRequest = pendingSignups.get(otpValidationRequest.getSignupId());
    if (signupRequest == null) {
      throw new SignupRequestNotFoundException("Signup request not found");
    }

    User newUser = createUser(signupRequest);
    createProfile(newUser);

    // Clean up
    otpStorage.remove(otpValidationRequest.getSignupId());
    pendingSignups.remove(otpValidationRequest.getSignupId());

    return new MessageResponse("User registered successfully!");
  }

  private User createUser(SignupRequest signupRequest) {
    User newUser = User.builder()
        .password(encoder.encode(signupRequest.getPassword()))
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .isActive(true)
        .build();

    String contactInfo = signupRequest.getEmailOrMobile();
    if (isEmail(contactInfo)) {
      newUser.setEmail(contactInfo);
      newUser.setIsEmailVerified(true);
      newUser.setIsMobileVerified(false);
    } else {
      newUser.setMobile(contactInfo);
      newUser.setIsEmailVerified(false);
      newUser.setIsMobileVerified(true);
    }

    Set<Role> roles = new HashSet<>();
    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
        .orElseThrow(() -> new RoleNotFoundException("Error: Role is not found."));
    roles.add(userRole);
    newUser.setRoles(roles);

    return userRepository.save(newUser);
  }

  private void createProfile(User user) {
    Profile profile = Profile.builder()
        .firstName("Guest")
        .lastName("User")
        .user(user)
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();

    profileRepository.save(profile);
  }

  private String generateOtp() {
    return String.format("%06d", (int) (Math.random() * 1000000));
  }

  private void sendEmailOtp(String email, String otp) {
    // Implement email sending logic
    System.out.println("Email OTP sent to " + email + ": " + otp);
  }

  private void sendMobileOtp(String mobile, String otp) {
    // Implement SMS sending logic
    System.out.println("Mobile OTP sent to " + mobile + ": " + otp);
  }

  private boolean isEmail(String input) {
    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    Pattern pattern = Pattern.compile(emailRegex);
    return pattern.matcher(input).matches();
  }

}
