package com.beat.matrimonial.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beat.matrimonial.enums.ERole;
import com.beat.matrimonial.entity.Role;
import com.beat.matrimonial.entity.User;
import com.beat.matrimonial.payload.request.LoginRequest;
import com.beat.matrimonial.payload.request.SignupRequest;
import com.beat.matrimonial.payload.response.JwtResponse;
import com.beat.matrimonial.payload.response.MessageResponse;
import com.beat.matrimonial.repository.RoleRepository;
import com.beat.matrimonial.repository.UserRepository;
import com.beat.matrimonial.security.jwt.JwtUtils;
import com.beat.matrimonial.security.service.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt,
        userDetails.getId(),
        userDetails.getUsername(),
        roles));
  }

  /**
   * Creates new User
   *
   * @param signUpRequest the user
   * @return the user
   */

  @Tag(name = "User", description = "User API")
  @PostMapping("/signup")
  @Operation(summary = "Creates user", description = "Returns created user", tags = "User")
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = User.builder()
        .name(signUpRequest.getName())
        .email(signUpRequest.getEmail())
        .password(encoder.encode(signUpRequest.getPassword()))
        .dateOfBirth(signUpRequest.getDateOfBirth())
        .gender(signUpRequest.getGender())
        .religion(signUpRequest.getReligion())
        .occupation(signUpRequest.getOccupation())
        .build();

    Set<Role> roles = new HashSet<>();

    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    roles.add(userRole);

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }
}