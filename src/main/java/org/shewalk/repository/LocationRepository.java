package org.shewalk.repository;

import org.shewalk.model.EmergencySession;
import org.shewalk.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location,Long> {

    List<Location> findByEmergencySession(EmergencySession session);

    Optional<Location>findTopByEmergencySessionOrderByRecordedAtDesc(EmergencySession session);
}
