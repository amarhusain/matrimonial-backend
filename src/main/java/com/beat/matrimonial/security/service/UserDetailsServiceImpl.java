package com.beat.matrimonial.security.service;


import com.beat.matrimonial.entity.User;
import com.beat.matrimonial.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String emailOrMobile) throws UsernameNotFoundException {
    User user = userRepository.findByEmailOrMobile(emailOrMobile, emailOrMobile)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

    return UserDetailsImpl.build(user);
  }

}
