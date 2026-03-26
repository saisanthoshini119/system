package com.example.system.controller;

import com.example.system.dto.AuthRequest;
import com.example.system.dto.AuthResponse;
import com.example.system.dto.RegisterRequest;
import com.example.system.model.User;
import com.example.system.repository.UserRepository;
import com.example.system.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        // For STUDENT and STAFF, branch is mandatory so that complaints can be
        // filtered correctly by branch on the staff dashboard.
        User.Role role = request.getRole() != null ? request.getRole() : User.Role.STUDENT;
        if ((role == User.Role.STUDENT || role == User.Role.STAFF)) {
            String branch = request.getBranch();
            if (branch == null || branch.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Error: Branch is required for students and staff.");
            }
        }

        User user = new User(
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                role,
                request.getDepartment(),
                request.getBranch()
        );

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            if (authentication.isAuthenticated()) {
                User user = userRepository.findByEmail(request.getEmail()).get();
                String token = jwtService.generateToken(user.getEmail());
                return ResponseEntity.ok(new AuthResponse(
                        token,
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getDepartment(),
                        user.getBranch()
                ));
            }
            return ResponseEntity.status(401).body("Invalid credentials");
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
