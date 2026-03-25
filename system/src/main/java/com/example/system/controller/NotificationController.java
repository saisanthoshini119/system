package com.example.system.controller;

import com.example.system.model.Notification;
import com.example.system.model.User;
import com.example.system.repository.UserRepository;
import com.example.system.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:3000")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        return ResponseEntity.ok(notificationService.getNotificationsForUser(user));
    }

    @GetMapping("/unread/count")
    public ResponseEntity<Long> getUnreadCount(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        return ResponseEntity.ok((long) notificationService.getUnreadNotificationsForUser(user).size());
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/read-all")
    public ResponseEntity<?> markAllAsRead(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        notificationService.markAllAsRead(user);
        return ResponseEntity.ok().build();
    }
}
