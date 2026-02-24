package org.shewalk.controller;

import org.shewalk.Dto.LocationRequestDto;
import org.shewalk.service.EmergencySessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/emergency")
public class EmergencyController {

    private final EmergencySessionService emergencySessionService;

    public EmergencyController(EmergencySessionService emergencySessionService){
        this.emergencySessionService = emergencySessionService;
    }

    @PostMapping("/start")
    public ResponseEntity<?> startEmergency(Authentication authentication, @RequestBody LocationRequestDto locationDto) {

        String email = authentication.getName();

        String trackingToken = emergencySessionService.startEmergency(email, locationDto.getLatitude(), locationDto.getLongitude());

        return ResponseEntity.ok(trackingToken);
    }

    @PostMapping("/stop")
    public ResponseEntity<?> stopEmergency(Authentication authentication) {

        String email = authentication.getName();

        emergencySessionService.stopEmergency(email);

        return ResponseEntity.ok("Emergency stopped successfully");
    }
}
