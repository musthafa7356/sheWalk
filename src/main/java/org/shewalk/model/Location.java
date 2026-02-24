package org.shewalk.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "session_id",  nullable = false)
    private EmergencySession emergencySession;

    private double latitude;
    private double longitude;

    private LocalDateTime recordedAt;

    @PrePersist
    public void onCreate() {
        this.recordedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public EmergencySession getEmergencySession() {
        return emergencySession;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setEmergencySession(EmergencySession emergencySession) {
        this.emergencySession = emergencySession;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
