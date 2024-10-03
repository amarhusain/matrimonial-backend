package com.beat.matrimonial.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {

//  @NotBlank
//  @Size(min = 3, max = 20)
//  private String name;

  @NotBlank
  @Size(max = 100)
  @Email
  private String emailOrMobile;

  @NotBlank
  @Size(min = 4, max = 40)
  private String password;

//  @NotBlank
//  @Size(min = 1, max = 10)
//  private String gender;
//
//  @NotBlank
//  @Size(min = 3, max = 50)
//  private String religion;
//
//
//  @NotBlank
//  @Size(min = 3, max = 50)
//  private String occupation;

  private String recaptcha;

  private Boolean termsAccepted;


}
