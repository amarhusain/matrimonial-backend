package com.beat.matrimonial.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoDTO {

  private Resource photo;
  private String contentType;
  private long contentLength;
}
