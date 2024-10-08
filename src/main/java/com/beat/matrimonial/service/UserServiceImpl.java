package com.beat.matrimonial.service;

import com.beat.matrimonial.entity.User;
import com.beat.matrimonial.exception.BadRequestException;
import com.beat.matrimonial.exception.ResourceNotFoundException;
import com.beat.matrimonial.payload.response.MessageResponse;
import com.beat.matrimonial.payload.response.UserResponse;
import com.beat.matrimonial.repository.UserRepository;
import java.lang.reflect.Field;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final AuthService authService;

  public UserServiceImpl(
      AuthService authService,
      UserRepository userRepository) {
    this.authService = authService;
    this.userRepository = userRepository;
  }


  @Override
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @Override
  public UserResponse getUserById(Long id) {
    if (id < 0) {
      throw new BadRequestException("Invalid ID: " + id);
    }
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

    return UserResponse.builder()
        .id(user.getId())
        .email(user.getEmail())
        .build();
  }

  @Override
  public User updateUser(Long id, User user) {
    User existingUser = userRepository.findById(id).orElse(null);
    if (existingUser != null) {
      return userRepository.save(existingUser);
    }
    return null;
  }

  @Override
  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }


  @Override
  public MessageResponse updateUserField(String fieldName, Object value) {
    User user = authService.getCurrentUser();
    try {
      Field field = User.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(user, value);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException("Error updating field: " + fieldName, e);
    }
    User updatedUser = userRepository.save(user);
    return new MessageResponse("User Updated sussessfully");
  }


}
