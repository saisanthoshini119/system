package com.example.system.controller;

import com.example.system.model.User;
import com.example.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.system.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
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
    public ResponseEntity<User> getProfile(Authentication authentication) {
        return userService.getUserByEmail(authentication.getName())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(Authentication authentication, @RequestBody User updatedUser) {
        return userService.getUserByEmail(authentication.getName())
                .map(existingUser -> ResponseEntity.ok(userService.updateUser(existingUser, updatedUser)))
                .orElse(ResponseEntity.notFound().build());
    }
}
