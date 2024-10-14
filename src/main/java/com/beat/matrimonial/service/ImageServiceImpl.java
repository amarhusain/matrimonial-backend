package com.beat.matrimonial.service;


import com.beat.matrimonial.awss3.S3Service;
import com.beat.matrimonial.entity.Profile;
import com.beat.matrimonial.exception.ResourceNotFoundException;
import com.beat.matrimonial.payload.response.MessageResponse;
import com.beat.matrimonial.repository.ProfileRepository;
import com.beat.matrimonial.security.jwt.JwtUtils;
import java.io.IOException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageServiceImpl implements ImageService {

  private final ProfileRepository profileRepository;
  private final S3Service s3Service;
  private final AuthService authService;
  private final ProfileService profileService;
  private final JwtUtils jwtUtils;

  private boolean openCvLoaded = false;

  @Autowired
  public ImageServiceImpl(ProfileRepository profileRepository, S3Service s3Service,
      AuthService authService, ProfileService profileService, JwtUtils jwtUtils) {
    this.profileRepository = profileRepository;
    this.s3Service = s3Service;
    this.authService = authService;
    this.profileService = profileService;
    this.jwtUtils = jwtUtils;
  }


  @Override
  public MessageResponse uploadProfileImage(MultipartFile file) throws IOException {
//    String fileName = file.getOriginalFilename();
//    Path filePath = Paths.get(uploadDir + fileName);
//    Files.createDirectories(filePath.getParent());
//    Files.write(filePath, file.getBytes());
    try {
      String fileName = s3Service.uploadImage(file);
      Profile profile = profileService.getCurrentUserProfile();
      profile.setPhotoUrl(fileName);
      profileRepository.save(profile);
    } catch (IOException e) {
      throw new IOException("Failed to upload image");
    }
    return new MessageResponse("Profile picture uploaded successfully");
  }

  @Override
  public ResponseEntity<byte[]> downloadProfileImage(String fileName) {
    Profile profile = profileService.getCurrentUserProfile();
    if (fileName.compareTo(profile.getPhotoUrl()) != 0) {
      throw new ResourceNotFoundException("Image not found");
    }

    try {
      byte[] imageBytes = s3Service.downloadImage(fileName);
      String contentType = s3Service.getContentType(fileName);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.parseMediaType(contentType));
      headers.setContentLength(imageBytes.length);

      return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<Resource> downloadImageBase65(String fileName, String token) {

    try {
      byte[] imageData = s3Service.downloadImage(fileName);

//      BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));

      if (!isUserAuthorized(token)) {
        imageData = blurImage(imageData);
        // Re-encode the blurred image
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ImageIO.write(image, "jpg", baos);
//        imageData = baos.toByteArray();
      }

      ByteArrayResource resource = new ByteArrayResource(imageData);

      return ResponseEntity.ok()
          .contentType(MediaType.IMAGE_JPEG)
          .contentLength(imageData.length)
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
          .body(resource);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private boolean isUserAuthorized(String token) {
    try {
      return token != null && !token.trim().isEmpty() && jwtUtils.validateJwtToken(token);
    } catch (Exception e) {
      return false;
    }
  }

  private byte[] blurImage(byte[] imageData) {
   
    // Convert byte array to Mat
    Mat src = Imgcodecs.imdecode(new MatOfByte(imageData), Imgcodecs.IMREAD_COLOR);
    Mat dst = new Mat();

    // Apply Gaussian Blur
    // Increase kernel size for stronger blur
    // Step 1: Apply a strong Gaussian blur
    int kernelSize = 151; // Very large kernel for strong blur
    Imgproc.GaussianBlur(src, dst, new Size(kernelSize, kernelSize), 0);

    // Step 2: Reduce the image resolution
    Mat reducedRes = new Mat();
    Imgproc.resize(dst, reducedRes, new Size(src.width() / 8, src.height() / 8), 0, 0,
        Imgproc.INTER_LINEAR);

    // Step 3: Scale back up to original size with NEAREST neighbor interpolation
    Imgproc.resize(reducedRes, dst, src.size(), 0, 0, Imgproc.INTER_NEAREST);

    // Step 4: Apply another Gaussian blur
    Imgproc.GaussianBlur(dst, dst, new Size(51, 51), 0);
    // Add this after the final Gaussian blur for an even stronger effect
    Imgproc.blur(dst, dst, new Size(31, 31));

    // Encode the blurred image back to byte array
    MatOfByte matOfByte = new MatOfByte();
    Imgcodecs.imencode(".jpg", dst, matOfByte);

    return matOfByte.toArray();
  }

}
