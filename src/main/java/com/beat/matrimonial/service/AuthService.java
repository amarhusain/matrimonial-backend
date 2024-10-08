package com.beat.matrimonial.service;

import com.beat.matrimonial.entity.User;
import com.beat.matrimonial.payload.request.LoginRequest;
import com.beat.matrimonial.payload.request.OtpValidationRequest;
import com.beat.matrimonial.payload.request.SignupRequest;
import com.beat.matrimonial.payload.response.JwtResponse;
import com.beat.matrimonial.payload.response.MessageResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {

  public JwtResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest);

  public String initiateSignup(@Valid @RequestBody SignupRequest signUpRequest);

  public MessageResponse validateOtpAndCompleteSignup(
      @Valid OtpValidationRequest otpValidationRequest);

  User getCurrentUser();

}
