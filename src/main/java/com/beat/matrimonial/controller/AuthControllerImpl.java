package com.beat.matrimonial.controller;

import com.beat.matrimonial.payload.request.LoginRequest;
import com.beat.matrimonial.payload.request.OtpValidationRequest;
import com.beat.matrimonial.payload.request.SignupRequest;
import com.beat.matrimonial.payload.response.JwtResponse;
import com.beat.matrimonial.payload.response.MessageResponse;
import com.beat.matrimonial.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthControllerImpl implements AuthController {

  private final AuthService authService;

  @Autowired
  public AuthControllerImpl(AuthService authService) {
    this.authService = authService;
  }


  @Override
  @PostMapping("/signin")
  public ResponseEntity<JwtResponse> authenticateUser(
      @Valid @RequestBody LoginRequest loginRequest) {
    JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
    if (jwtResponse != null) {
      return ResponseEntity.ok(jwtResponse);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @Override
  @PostMapping("/signup")
  public ResponseEntity<MessageResponse> validateOtpAndCompleteSignup(
      @Valid @RequestBody OtpValidationRequest otpValidationRequest) {
    MessageResponse messageResponse = authService.validateOtpAndCompleteSignup(
        otpValidationRequest);
    return ResponseEntity.ok(messageResponse);
  }

  @Override
  @PostMapping("/initiate-signup")
  public ResponseEntity<String> initiateSignup(
      @Valid @RequestBody SignupRequest signUpRequest) {
    String messageResponse = authService.initiateSignup(signUpRequest);
    if (messageResponse != null) {
      return ResponseEntity.ok(messageResponse);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

}