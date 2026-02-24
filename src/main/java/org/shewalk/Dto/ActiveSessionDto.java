package org.shewalk.Dto;

import java.time.LocalDateTime;

public class ActiveSessionDto {

    private Long id;
    private String userName;
    private LocalDateTime startedAt;
    private String trackingToken;

    public ActiveSessionDto(Long id, String userName,
                            LocalDateTime startedAt,
                            String trackingToken) {
        this.id = id;
        this.userName = userName;
        this.startedAt = startedAt;
        this.trackingToken = trackingToken;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public String getTrackingToken() {
        return trackingToken;
    }
}
