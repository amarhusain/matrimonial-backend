package com.beat.matrimonial.service;

import com.beat.matrimonial.payload.response.MessageResponse;
import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

  MessageResponse uploadProfileImage(MultipartFile file) throws IOException;

  ResponseEntity<byte[]> downloadProfileImage(String fileName);

  ResponseEntity<Resource> downloadImageBase65(String fileName, String token);
}
