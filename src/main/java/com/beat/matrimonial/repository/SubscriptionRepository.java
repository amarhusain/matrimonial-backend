package com.beat.matrimonial.repository;

import com.beat.matrimonial.entity.Subscription;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

  // Find the active subscription for a specific profile
  Optional<Subscription> findFirstByProfileIdAndIsActiveTrue(Long profileId);

}
