package com.beat.matrimonial.controller;

import com.beat.matrimonial.entity.User;
import com.beat.matrimonial.payload.response.MessageResponse;
import com.beat.matrimonial.payload.response.UserResponse;
import com.beat.matrimonial.service.UserServiceImpl;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserControllerImpl implements UserController {

  private final UserServiceImpl userService;

  @Autowired
  public UserControllerImpl(UserServiceImpl userService) {
    this.userService = userService;
  }

  /**
   * Get All Users
   *
   * @return user list
   */
  @Override
  public List<User> getAllUsers() {
    return userService.getAllUsers();
  }

  /**
   * Get User by id
   *
   * @param id the user id
   * @return user
   */
  @Override
  public UserResponse getUserById(@PathVariable Long id) {
    return userService.getUserById(id);
  }

  @PutMapping("/{id}")
  public User updateUser(@PathVariable Long id, @RequestBody User user) {
    return userService.updateUser(id, user);
  }

  @DeleteMapping("/{id}")
  public void deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
  }

  @Override
  @PatchMapping("/update-user-field")
  public ResponseEntity<MessageResponse> updateUserField(@RequestBody Map<String, Object> updates) {
    String field = updates.keySet().iterator().next(); // Get the first (and only) key
    Object value = updates.get(field);
    MessageResponse messageResponse = userService.updateUserField(field, value);
    return ResponseEntity.ok(messageResponse);
  }

}
