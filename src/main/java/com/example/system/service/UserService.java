package com.example.system.service;

import com.example.system.model.User;
import com.example.system.dto.ProfileUpdateRequest;
import com.example.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(User existingUser, ProfileUpdateRequest updatedDetails) {
        existingUser.setName(updatedDetails.getName());
        existingUser.setDepartment(updatedDetails.getDepartment());
        existingUser.setBranch(updatedDetails.getBranch());
        // For security, only update password if provided
        if (updatedDetails.getPassword() != null && !updatedDetails.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedDetails.getPassword()));
        }
        return userRepository.save(existingUser);
    }
}
