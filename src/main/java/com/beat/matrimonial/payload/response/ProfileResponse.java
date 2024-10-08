package com.beat.matrimonial.payload.response;

import com.beat.matrimonial.dto.ProfileDto;
import com.beat.matrimonial.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponse {

  private ProfileDto profile;
  private UserDto user;
  private int profileCompletion;

}
