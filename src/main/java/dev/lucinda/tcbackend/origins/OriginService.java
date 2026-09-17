package dev.lucinda.tcbackend.origins;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly=true)
class OriginService {

    private final OriginRepository originRepository;

    OriginService(OriginRepository originRepository) {
        this.originRepository = originRepository;
    }

    /* Methods */
    List<Origin> findAll() {
        return originRepository.findAll();
    }

    Optional<Origin> findBy(long id) {
        return originRepository.findById(id);
    }

}
