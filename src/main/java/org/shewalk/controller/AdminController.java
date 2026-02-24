package org.shewalk.controller;

import org.shewalk.Dto.ActiveSessionDto;
import org.shewalk.model.EmergencySession;
import org.shewalk.model.User;
import org.shewalk.repository.EmergencySessionRepository;
import org.shewalk.repository.UserRepository;
import org.shewalk.service.EmergencySessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final EmergencySessionRepository emergencySessionRepository;
    private final EmergencySessionService emergencySessionService;

    public AdminController(UserRepository userRepository,
                           EmergencySessionRepository emergencySessionRepository,EmergencySessionService emergencySessionService) {
        this.userRepository = userRepository;
        this.emergencySessionRepository = emergencySessionRepository;
        this.emergencySessionService = emergencySessionService;
    }

    // ✅ Get all registered users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/session/active")
    public ResponseEntity<List<ActiveSessionDto>> getActiveSessions() {
        return ResponseEntity.ok(
                emergencySessionService.getActiveSessionsForAdmin()
        );
    }

    // ✅ Force stop emergency session
    @PostMapping("/sessions/{id}/stop")
    public String forceStopSession(@PathVariable Long id) {

        EmergencySession session = emergencySessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        session.setActive(false);
        session.setEndedAt(LocalDateTime.now());

        emergencySessionRepository.save(session);

        return "Emergency session stopped successfully";
    }

    @GetMapping("/sessions/history")
    public ResponseEntity<?> getAllSessions() {
        return ResponseEntity.ok(
                emergencySessionService.getAllSessionsForAdmin()
        );
    }
}