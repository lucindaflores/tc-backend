package dev.lucindaflores.tcbackend.origins;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OriginRepository extends JpaRepository<Origin, Long> {

    Optional<Origin> findByName(String name);

}
