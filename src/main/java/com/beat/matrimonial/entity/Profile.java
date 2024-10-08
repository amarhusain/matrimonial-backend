package com.beat.matrimonial.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "profiles")
public class Profile {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "first_name", nullable = true, length = 100)
  private String firstName;

  @Column(name = "middle_name", nullable = true, length = 100)
  private String middleName;

  @Column(name = "last_name", nullable = true, length = 100)
  private String lastName;


  @Column(name = "date_of_birth", nullable = true)
  private LocalDate dateOfBirth;

  @Column(name = "gender", nullable = true, length = 10)
  private String gender;

  @Column(name = "religion", nullable = true, length = 50)
  private String religion;

  @Column(name = "sect", nullable = true, length = 50)
  private String sect;


  @Column(name = "occupation", nullable = true, length = 100)
  private String occupation;

  @Column(name = "address", nullable = true, length = 100)
  private String address;

  @Column(name = "city", nullable = true, length = 100)
  private String city;

  @Column(name = "state", nullable = true, length = 100)
  private String state;

  @Column(name = "country", length = 50)
  private String country;

  @Column(name = "profile_picture_url")
  private String profilePictureUrl;

  @Column(name = "bio", length = 50)
  private String bio;

  @JsonIgnore
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @JsonIgnore
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

}
