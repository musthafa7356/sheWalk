package org.shewalk.service;


import org.shewalk.Dto.ActiveSessionDto;
import org.shewalk.Dto.AdminSessionDto;
import org.shewalk.Dto.TrackingLocationDto;
import org.shewalk.exception.BadRequestException;
import org.shewalk.exception.ResourceNotFoundException;
import org.shewalk.model.EmergencySession;
import org.shewalk.model.Location;
import org.shewalk.model.User;
import org.shewalk.repository.EmergencySessionRepository;
import org.shewalk.repository.LocationRepository;
import org.shewalk.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmergencySessionService {

    private final EmergencySessionRepository emergencySessionRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final EmailService emailService;

    public EmergencySessionService(EmergencySessionRepository emergencySessionRepository, UserRepository userRepository, LocationRepository locationRepository,  EmailService emailService) {
        this.emergencySessionRepository = emergencySessionRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.emailService = emailService;
    }

    public String startEmergency(String email, double latitude, double longitude) {

        // ✅ Latitude validation
        if (latitude < -90 || latitude > 90) {
            throw new BadRequestException("Invalid latitude value");
        }

        // ✅ Longitude validation
        if (longitude < -180 || longitude > 180) {
            throw new BadRequestException("Invalid longitude value");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        emergencySessionRepository.findByUserAndIsActiveTrue(user)
                .ifPresent(session -> {
                    throw new BadRequestException("Emergency already active");
                });

        EmergencySession session = new EmergencySession();
        session.setUser(user);

        EmergencySession saved = emergencySessionRepository.save(session);

        Location location = new Location();
        location.setEmergencySession(saved);
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        locationRepository.save(location);

        //send email

        String baseUrl = System.getenv("APP_BASE_URL");
        String trackingLink = baseUrl + "/track.html?token=" + saved.getTrackingToken();

        emailService.sendTrackingLink(user.getTrustedEmail(), trackingLink);

        return saved.getTrackingToken();
    }

    public void stopEmergency(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        EmergencySession session = emergencySessionRepository.findByUserAndIsActiveTrue(user)
                .orElseThrow(() -> new ResourceNotFoundException("No active emergency found"));

        session.setActive(false);
        session.setEndedAt(LocalDateTime.now());

        emergencySessionRepository.save(session);
    }

    public TrackingLocationDto getLatestLocation(String trackingToken) {

        EmergencySession session = emergencySessionRepository
                .findByTrackingToken(trackingToken)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid tracking token"));

        if (!session.isActive()) {
            throw new BadRequestException("Emergency session ended");
        }

        Location location = locationRepository
                .findTopByEmergencySessionOrderByRecordedAtDesc(session)
                .orElseThrow(() -> new ResourceNotFoundException("No location found"));

        return new TrackingLocationDto(
                location.getLatitude(),
                location.getLongitude(),
                location.getRecordedAt()
        );
    }

    public List<AdminSessionDto> getAllSessionsForAdmin() {

        return emergencySessionRepository
                .findAllByOrderByStartedAtDesc()
                .stream()
                .map(session -> {

                    String durationText;

                    if (session.getEndedAt() != null) {
                        Duration duration = Duration.between(
                                session.getStartedAt(),
                                session.getEndedAt()
                        );

                        long minutes = duration.toMinutes();
                        durationText = minutes + " minutes";
                    } else {
                        durationText = "Ongoing";
                    }

                    return new AdminSessionDto(
                            session.getId(),
                            session.getUser().getName(),
                            session.getUser().getEmail(),
                            session.getStartedAt(),
                            session.getEndedAt(),
                            session.isActive(),
                            durationText
                    );
                })
                .collect(Collectors.toList());
    }

    public List<ActiveSessionDto> getActiveSessionsForAdmin() {

        return emergencySessionRepository.findByIsActiveTrue()
                .stream()
                .map(session -> new ActiveSessionDto(
                        session.getId(),
                        session.getUser().getName(),
                        session.getStartedAt(),
                        session.getTrackingToken()
                ))
                .collect(Collectors.toList());
    }
}
