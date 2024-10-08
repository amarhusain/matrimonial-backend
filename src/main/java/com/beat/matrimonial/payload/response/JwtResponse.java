package com.beat.matrimonial.payload.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse {

  private String token;
  private Long id;
  private String email;
  private String firstName;
  private String middleName;
  private String lastName;
  private String profilePictureUrl;

  private List<String> roles;


}
