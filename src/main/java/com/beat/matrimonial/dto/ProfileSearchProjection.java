package com.beat.matrimonial.dto;

import java.time.LocalDate;

public interface ProfileSearchProjection {

  Long getId();

  String getFirstName();

  String getMiddleName();

  String getLastName();

  LocalDate getDateOfBirth();

  String getReligion();

  String getSect();

  String getOccupation();

  String getCity();

  String getState();

  String getCountry();

  String getHeight();

  String getIncome();

  String getMaritalStatus();

  String getWorkplace();

  String getPhotoUrl();
}
