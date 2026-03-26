package com.example.system.controller;

import com.example.system.dto.ProfileResponse;
import com.example.system.dto.ProfileUpdateRequest;
import com.example.system.model.User;
import com.example.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.system.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/staff")
    public ResponseEntity<java.util.List<User>> getStaff() {
        return ResponseEntity.ok(userRepository.findByRole(User.Role.STAFF));
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getProfile(Authentication authentication) {
        return userService.getUserByEmail(authentication.getName())
                .map(user -> ResponseEntity.ok(new ProfileResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getDepartment(),
                        user.getBranch()
                )))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/profile")
    public ResponseEntity<ProfileResponse> updateProfile(Authentication authentication, @RequestBody ProfileUpdateRequest updatedUser) {
        return userService.getUserByEmail(authentication.getName())
                .map(existingUser -> {
                    User savedUser = userService.updateUser(existingUser, updatedUser);
                    return ResponseEntity.ok(new ProfileResponse(
                            savedUser.getId(),
                            savedUser.getName(),
                            savedUser.getEmail(),
                            savedUser.getRole(),
                            savedUser.getDepartment(),
                            savedUser.getBranch()
                    ));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
