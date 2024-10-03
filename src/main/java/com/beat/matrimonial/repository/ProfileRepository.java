package com.beat.matrimonial.repository;

import com.beat.matrimonial.entity.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends CrudRepository<Profile, Long> {

  Profile findByUserId(Long userId);
}
