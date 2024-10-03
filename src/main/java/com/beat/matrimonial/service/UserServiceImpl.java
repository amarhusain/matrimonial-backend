package com.beat.matrimonial.service;

import com.beat.matrimonial.entity.User;
import com.beat.matrimonial.exception.BadRequestException;
import com.beat.matrimonial.exception.ResourceNotFoundException;
import com.beat.matrimonial.payload.response.UserResponse;
import com.beat.matrimonial.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
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


}
