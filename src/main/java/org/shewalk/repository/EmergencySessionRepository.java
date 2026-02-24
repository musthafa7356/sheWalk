package org.shewalk.repository;

import org.shewalk.model.EmergencySession;
import org.shewalk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmergencySessionRepository extends JpaRepository<EmergencySession, Long> {

    Optional<EmergencySession> findByTrackingToken(String trackingToken);

    Optional<EmergencySession> findByUserAndIsActiveTrue(User user);

    List<EmergencySession> findByIsActiveTrue();

    List<EmergencySession> findAllByOrderByStartedAtDesc();

}
