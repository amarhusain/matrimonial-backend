package com.beat.matrimonial.controller;

import com.beat.matrimonial.entity.Profile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


/**
 * The interface User api
 */
@Tag(name = "Image", description = "Image API")
@RequestMapping(value = "/api/images", produces = MediaType.APPLICATION_JSON_VALUE)
public interface ImageController {


  @PostMapping()
  @Operation(summary = "Upload user image", description = "Upload user image", tags = "Profile APIs")
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = {
          @Content(schema = @Schema(implementation = Profile.class), mediaType = "application/json")}),
      @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
      @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
  ResponseEntity<?> uploadProfileImage(@RequestParam("file") MultipartFile file);

  @GetMapping("/{fileName}")
  @Operation(summary = "Get user image", description = "Get user image", tags = "Profile APIs")
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = {
          @Content(schema = @Schema(implementation = Profile.class), mediaType = "application/json")}),
      @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
      @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
  ResponseEntity<byte[]> downloadProfileImage(@PathVariable String fileName);

  @GetMapping("/base65/{fileName}")
  @Operation(summary = "Get user image", description = "Get user image", tags = "Profile APIs")
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = {
          @Content(schema = @Schema(implementation = Profile.class))}),
      @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
      @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
  ResponseEntity<Resource> downloadImageBase65(@PathVariable String fileName,
      @RequestParam(required = false) String token);

}
