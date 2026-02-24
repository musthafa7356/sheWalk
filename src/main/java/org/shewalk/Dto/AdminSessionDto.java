package org.shewalk.Dto;

import java.time.LocalDateTime;

public class AdminSessionDto {

    private Long id;
    private String userName;
    private String userEmail;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private boolean active;
    private String duration;

    public AdminSessionDto(Long id, String userName, String userEmail,
                           LocalDateTime startedAt, LocalDateTime endedAt,
                           boolean active, String duration) {
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.active = active;
        this.duration = duration;
    }

    public Long getId() { return id; }
    public String getUserName() { return userName; }
    public String getUserEmail() { return userEmail; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public LocalDateTime getEndedAt() { return endedAt; }
    public boolean isActive() { return active; }
    public String getDuration() { return duration; }
}
