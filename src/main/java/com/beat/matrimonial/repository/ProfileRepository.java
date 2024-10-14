package com.beat.matrimonial.repository;

import com.beat.matrimonial.dto.ProfileSearchProjection;
import com.beat.matrimonial.dto.SearchCriteria;
import com.beat.matrimonial.entity.Profile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


  @Query("SELECT p FROM Profile p WHERE " +
      "(:#{#criteria.lookingFor} IS NULL OR " +
      "(:#{#criteria.lookingFor} = 'bride' AND p.gender = 'Female') OR " +
      "(:#{#criteria.lookingFor} = 'groom' AND p.gender = 'Male')) AND " +
      "(:#{#criteria.minAge} IS NULL OR (YEAR(CURRENT_DATE) - YEAR(p.dateOfBirth)) >= :#{#criteria.minAge}) AND "
      +
      "(:#{#criteria.maxAge} IS NULL OR (YEAR(CURRENT_DATE) - YEAR(p.dateOfBirth)) <= :#{#criteria.maxAge}) AND "
      +
      "(:#{#criteria.minHeight} IS NULL OR p.height >= :#{#criteria.minHeight}) AND " +
      "(:#{#criteria.maxHeight} IS NULL OR p.height <= :#{#criteria.maxHeight}) AND " +
      "(:#{#criteria.religion} IS NULL OR p.religion = :#{#criteria.religion}) AND " +
      "(:#{#criteria.sect} IS NULL OR p.sect = :#{#criteria.sect}) AND " +
      "(:#{#criteria.minIncome} IS NULL OR p.income >= :#{#criteria.minIncome}) AND " +
      "(:#{#criteria.maxIncome} IS NULL OR p.income <= :#{#criteria.maxIncome}) AND " +
      "(:#{#criteria.maritalStatus} IS NULL OR p.maritalStatus = :#{#criteria.maritalStatus}) AND "
      +
      // Handle profilePhoto criteria correctly
      "((:#{#criteria.profilePhoto} = 'yes' AND p.photoUrl IS NOT NULL) OR " +
      " (:#{#criteria.profilePhoto} = 'no' AND p.photoUrl IS NULL OR p.photoUrl IS NOT NULL) OR "
      +
      " (:#{#criteria.profilePhoto} IS NULL))")
  Page<ProfileSearchProjection> findProfileByCriteria(SearchCriteria criteria, Pageable pageable);

}
