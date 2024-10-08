package com.beat.matrimonial.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

  private Long id;
  private String email;
  private String mobile;
  private Boolean isActive;
  private Boolean isMobileVerified;
  private Boolean isEmailVerified;
}
