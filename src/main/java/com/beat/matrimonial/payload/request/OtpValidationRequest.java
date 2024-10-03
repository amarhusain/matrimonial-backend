package com.beat.matrimonial.payload.request;

import lombok.Data;

@Data
public class OtpValidationRequest {

  private String signupId;
  private String otp;
}
