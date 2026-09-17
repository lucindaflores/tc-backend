package dev.lucindaflores.tcbackend.materials;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface MaterialRepository extends JpaRepository<Material, Long> {

    Optional<Material> findByName(String name);

}
