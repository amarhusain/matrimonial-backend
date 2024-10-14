package com.beat.matrimonial.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileSearchDTO {

  private Long id;
  private String firstName;
  private String middleName;
  private String lastName;
  private LocalDate dateOfBirth;
  private String religion;
  private String sect;
  private String occupation;
  private String city;
  private String state;
  private String country;
  private String height;
  private String income;
  private String maritalStatus;
  private String workplace;
  private String photoUrl;

}
