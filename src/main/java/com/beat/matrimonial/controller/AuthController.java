package com.beat.matrimonial.controller;

import com.beat.matrimonial.payload.request.LoginRequest;
import com.beat.matrimonial.payload.request.OtpValidationRequest;
import com.beat.matrimonial.payload.request.SignupRequest;
import com.beat.matrimonial.payload.response.JwtResponse;
import com.beat.matrimonial.payload.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/auth")
public interface AuthController {


  /**
   * Creates new User
   *
   * @param loginRequest the user
   * @return the user
   */
  @Tag(name = "Auth", description = "Auth API")
  @PostMapping("/signin")
  @Operation(summary = "Authenticate user", description = "Returns Jwt", tags = "User")
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = {
          @Content(schema = @Schema(implementation = JwtResponse.class), mediaType = "application/json")}),
      @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
      @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
  public ResponseEntity<JwtResponse> authenticateUser(
      @Valid @RequestBody LoginRequest loginRequest);

  /**
   * Creates new User
   *
   * @param otpValidationRequest the user
   * @return the user
   */
  @Tag(name = "Auth", description = "Auth API")
  @PostMapping("/signup")
  @Operation(summary = "Creates user", description = "Returns created user", tags = "User")
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = {
          @Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json")}),
      @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
      @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
  public ResponseEntity<MessageResponse> validateOtpAndCompleteSignup(
      @Valid @RequestBody OtpValidationRequest otpValidationRequest);

  /**
   * Initiates Signup for new User
   *
   * @param signUpRequest the user
   * @return the otp
   */
  @Tag(name = "Auth", description = "Auth API")
  @PostMapping("/initiate-signup")
  @Operation(summary = "Creates user", description = "Returns created user", tags = "User")
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = {
          @Content(schema = @Schema(implementation = String.class), mediaType = "application/json")}),
      @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
      @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
  public ResponseEntity<String> initiateSignup(
      @Valid @RequestBody SignupRequest signUpRequest);

}