package com.beat.matrimonial.controller;

import com.beat.matrimonial.entity.Profile;
import com.beat.matrimonial.payload.response.MessageResponse;
import com.beat.matrimonial.payload.response.ProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * The interface User api
 */
@Tag(name = "Profile", description = "Profile API")
@RequestMapping(value = "/api/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
public interface ProfileController {

  @GetMapping("/{userId}")
  @Operation(summary = "Get profile by user id", description = "Returns profile", tags = "Profile APIs")
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = {
          @Content(schema = @Schema(implementation = Profile.class), mediaType = "application/json")}),
      @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
      @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
  public ResponseEntity<ProfileResponse> getProfileByUserId(@PathVariable Long userId);

  @PutMapping("/{userId}")
  @Operation(summary = "Update user profile", description = "Updates profile", tags = "Profile APIs")
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = {
          @Content(schema = @Schema(implementation = Profile.class), mediaType = "application/json")}),
      @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
      @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
  public ResponseEntity<Profile> updateProfile(@PathVariable Long userId,
      @RequestBody Profile profile);


  @PatchMapping("/update-profile-field")
  @Operation(summary = "Update user profile partially", description = "Updates profile partially", tags = "Profile APIs")
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = {
          @Content(schema = @Schema(implementation = Profile.class), mediaType = "application/json")}),
      @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
      @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
  public ResponseEntity<MessageResponse> updateProfileField(
      @RequestBody Map<String, Object> updates);

  @PutMapping("/religion")
  @Operation(summary = "Update religion, sect partially", description = "Updates religion, sect partially", tags = "Profile APIs")
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = {
          @Content(schema = @Schema(implementation = Profile.class), mediaType = "application/json")}),
      @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
      @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
  public ResponseEntity<?> updateReligionAndSect(@RequestParam String religion,
      @RequestParam(required = false) String sect);


  @PostMapping("/image")
  @Operation(summary = "Upload user image", description = "Upload user image", tags = "Profile APIs")
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = {
          @Content(schema = @Schema(implementation = Profile.class), mediaType = "application/json")}),
      @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
      @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
  public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file);

  @GetMapping("/image")
  @Operation(summary = "Get user image", description = "Get user image", tags = "Profile APIs")
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = {
          @Content(schema = @Schema(implementation = Profile.class), mediaType = "application/json")}),
      @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
      @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
  public ResponseEntity<Resource> getProfileImage();

}
