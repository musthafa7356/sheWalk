package org.shewalk.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class EmergencySession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(unique = true, nullable = false)
    private String trackingToken;

    private boolean isActive;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    @PrePersist
    public void generateToken() {
        this.trackingToken = UUID.randomUUID().toString();
        this.startedAt = LocalDateTime.now();
        this.isActive = true;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getTrackingToken() {
        return trackingToken;
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }
}
