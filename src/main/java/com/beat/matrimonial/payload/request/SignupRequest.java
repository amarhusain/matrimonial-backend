package com.beat.matrimonial.payload.request;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SignupRequest {

  @NotBlank
  @Size(min = 3, max = 20)
  private String name;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;

  @NotBlank
  private LocalDate dateOfBirth;

  @NotBlank
  @Size(min = 1, max = 10)
  private String gender;

  @NotBlank
  @Size(min = 3, max = 50)
  private String religion;


  @NotBlank
  @Size(min = 3, max = 50)
  private String occupation;

  private String recaptcha;

  private Boolean termsAccepted;


}
