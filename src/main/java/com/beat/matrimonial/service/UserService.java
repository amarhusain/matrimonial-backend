package com.beat.matrimonial.service;

import com.beat.matrimonial.entity.User;
import com.beat.matrimonial.payload.response.MessageResponse;
import com.beat.matrimonial.payload.response.UserResponse;
import java.util.List;

public interface UserService {

  List<User> getAllUsers();

  UserResponse getUserById(Long id);

  User updateUser(Long id, User user);

  void deleteUser(Long id);

  MessageResponse updateUserField(String fieldName, Object value);
}
