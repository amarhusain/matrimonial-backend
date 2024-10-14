package com.beat.matrimonial.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {

  private String lookingFor;
  private Integer minAge;
  private Integer maxAge;
  private Integer minHeight;
  private Integer maxHeight;
  private String religion;
  private String sect;
  private Integer minIncome;
  private Integer maxIncome;
  private String maritalStatus;
  private String profilePhoto;

}
