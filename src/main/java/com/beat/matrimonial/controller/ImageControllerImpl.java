package com.beat.matrimonial.controller;

import com.beat.matrimonial.service.ImageService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/images")
public class ImageControllerImpl implements ImageController {


  private final ImageService imageService;

  @Autowired
  public ImageControllerImpl(ImageService imageService) {
    this.imageService = imageService;
  }

  @Override
  @PostMapping()
  public ResponseEntity<?> uploadProfileImage(@RequestParam("file") MultipartFile file) {
    try {
      return ResponseEntity.ok(imageService.uploadProfileImage(file));
    } catch (IOException e) {
      return ResponseEntity.internalServerError().body("Failed to upload image");
    }
  }

  @Override
  @GetMapping("/{fileName}")
  public ResponseEntity<byte[]> downloadProfileImage(@PathVariable String fileName) {
    return imageService.downloadProfileImage(fileName);
  }

  @Override
  @GetMapping("/base65/{fileName}")
  public ResponseEntity<Resource> downloadImageBase65(@PathVariable String fileName,
      @RequestParam(required = false) String token) {
    return imageService.downloadImageBase65(fileName, token);
  }

}
