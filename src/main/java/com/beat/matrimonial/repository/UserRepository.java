package com.beat.matrimonial.repository;

import com.beat.matrimonial.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmailOrMobile(String email, String mobile);
  
  Optional<User> findByEmail(String email);

  Boolean existsByEmail(String email);

  @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = ?1 OR u.mobile = ?1")
  boolean existsByEmailOrMobile(String emailOrMobile);


}
