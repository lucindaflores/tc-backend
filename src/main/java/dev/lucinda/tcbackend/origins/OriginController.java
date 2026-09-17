package dev.lucinda.tcbackend.origins;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("origins")
class OriginController {

    private final OriginService originService;

    OriginController(OriginService originService) {
        this.originService = originService;
    }

    /* DTO(s) */
    private record OriginName(long id, String name) {
        OriginName(Origin origin) {
            this(origin.getId(), origin.getName());
        }
    }

    /* Requests */
    // GET http://localhost:8080/origins
    @GetMapping
    List<OriginName> findAll() {
        return originService.findAll()
                .stream()
                .map(OriginName::new)
                .toList();
    }

    // GET http://localhost:8080/origins/id
    @GetMapping("{id}")
    OriginName findById(@PathVariable long id) {
        return originService.findBy(id)
                .map(OriginName::new)
                .orElseThrow(OriginNotFoundException::new);
    }

}
