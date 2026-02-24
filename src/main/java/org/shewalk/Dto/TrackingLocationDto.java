package org.shewalk.Dto;

import java.time.LocalDateTime;

public class TrackingLocationDto {

    private double latitude;
    private double longitude;
    private LocalDateTime recordedAt;

    public TrackingLocationDto(double latitude, double longitude, LocalDateTime recordedAt) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.recordedAt = recordedAt;
    }

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public LocalDateTime getRecordedAt() { return recordedAt; }
}
