package com.beat.matrimonial.service;

import com.beat.matrimonial.entity.Subscription;
import com.beat.matrimonial.repository.SubscriptionRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {


  private final SubscriptionRepository subscriptionRepository;

  @Autowired
  public SubscriptionService(SubscriptionRepository subscriptionRepository) {
    this.subscriptionRepository = subscriptionRepository;
  }


  // Check if the profile has an active subscription
  public boolean hasActiveSubscription(Long profileId) {
    Optional<Subscription> subscription = subscriptionRepository.findFirstByProfileIdAndIsActiveTrue(
        profileId);
    return subscription.isPresent() && subscription.get().isSubscriptionValid();
  }
}
