package com.beat.matrimonial.repository;

import com.beat.matrimonial.dto.ProfileSearchProjection;
import com.beat.matrimonial.entity.Profile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

  Optional<Profile> findByUserId(Long userId);

  @Query("SELECT p FROM Profile p WHERE " +
      "(:gender IS NULL OR p.gender = :gender) AND " +
      "(:ageStart IS NULL OR :ageEnd IS NULL OR " +
      "(YEAR(CURRENT_DATE) - YEAR(p.dateOfBirth)) BETWEEN :ageStart AND :ageEnd) AND " +
      "(:city IS NULL OR p.city = :city)")
  List<ProfileSearchProjection> findByCriteria(
      @Param("gender") String gender,
      @Param("ageStart") Integer ageStart,
      @Param("ageEnd") Integer ageEnd,
      @Param("city") String city);

}
