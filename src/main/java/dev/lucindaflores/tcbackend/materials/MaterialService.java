package dev.lucindaflores.tcbackend.materials;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly=true)
class MaterialService {

    private final MaterialRepository materialRepository;

    MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    /* Methods */
    List<Material> findAll() {
        return materialRepository.findAll();
    }

    Optional<Material> findBy(long id) {
        return materialRepository.findById(id);
    }

    Optional<Material> findByName(String name) {
        return materialRepository.findByName(name);
    }

}
