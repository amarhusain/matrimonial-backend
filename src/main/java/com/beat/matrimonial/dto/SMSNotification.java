package com.beat.matrimonial.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SMSNotification implements Serializable {

  private String phoneNumber;
  private String message;
}
