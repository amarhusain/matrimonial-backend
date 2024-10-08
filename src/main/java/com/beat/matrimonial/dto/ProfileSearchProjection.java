package com.beat.matrimonial.dto;

import java.time.LocalDate;

public interface ProfileSearchProjection {

  Long getId();

  String getFirstName();

  String getMiddleName();

  String getLastName();

  LocalDate getDateOfBirth();

  String getGender();

  String getReligion();

  String getSect();

  String getOccupation();

  String getCity();

  String getProfilePictureUrl();
}
