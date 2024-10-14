package com.beat.matrimonial.awss3;


import com.beat.matrimonial.dto.PhotoDTO;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

  @Value("${aws.s3.bucket}")
  private String bucketName;

  private final S3Client s3Client;

  @Autowired
  public S3Service(S3Client s3Client) {
    this.s3Client = s3Client;
  }

  public String uploadImage(MultipartFile file) throws IOException {
    String fileName = generateFileName(file);
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(fileName)
        .contentType(file.getContentType())
        .build();

    s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(),
        file.getSize()));

    return fileName;
  }

  public byte[] downloadImage(String fileName) throws IOException {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(fileName)
        .build();
    ResponseInputStream<GetObjectResponse> response = s3Client.getObject(getObjectRequest);
    return response.readAllBytes();
  }

  public PhotoDTO downloadImageBase65(String fileName) {

    try {
      GetObjectRequest getObjectRequest = GetObjectRequest.builder()
          .bucket(bucketName)
          .key(fileName)
          .build();

      ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
      GetObjectResponse objectResponse = s3Object.response();

      String contentType = objectResponse.contentType();
      if (contentType == null) {
        contentType = "application/octet-stream";
      }

      return PhotoDTO.builder()
          .photo(new InputStreamResource(s3Object))
          .contentType(contentType)
          .contentLength(objectResponse.contentLength())
          .build();
    } catch (NoSuchKeyException e) {
      // Log the error and return an appropriate response
      System.out.println(fileName + ": " + e.getMessage());
      return null;
    } catch (Exception e) {
      // Log the error and return an appropriate response
      e.printStackTrace();
      return null;
    }
  }

  public String getContentType(String fileName) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(fileName)
        .build();

    GetObjectResponse response = s3Client.getObject(getObjectRequest).response();
    return response.contentType();
  }

  private String generateFileName(MultipartFile file) {
    return UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
  }


}