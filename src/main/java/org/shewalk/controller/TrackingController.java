package org.shewalk.controller;


import org.shewalk.Dto.TrackingLocationDto;
import org.shewalk.model.Location;
import org.shewalk.service.EmergencySessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/track")
public class TrackingController {

    private final EmergencySessionService emergencySessionService;
    public TrackingController(EmergencySessionService emergencySessionService) {
        this.emergencySessionService = emergencySessionService;
    }

    @GetMapping("/{token}")
    public ResponseEntity<TrackingLocationDto> track(@PathVariable String token) {

        TrackingLocationDto location = emergencySessionService.getLatestLocation(token);

        if (location == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(location);
    }
}
