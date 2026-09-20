package dev.lucindaflores.tcbackend.materials;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("materials")
@CrossOrigin
class MaterialController {

    private final MaterialService materialService;

    MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    /* DTO(s) */
    private record MaterialDetails(
            long materialId,
            String name,
            String nameSpanish,
            String technique) {

        MaterialDetails(Material material) {
            this(material.getId(),
                 material.getName(),
                 material.getNameSpanish(),
                 material.getTechnique()
            );
        }
    }

    /* Requests*/
    // GET http://localhost:8080/materials
    @GetMapping
    List<MaterialDetails> findAll() {
        return materialService.findAll()
                .stream()
                .map(MaterialDetails::new)
                .toList();
    }

    // GET http://localhost:8080/materials/1
    @GetMapping("{id}")
    MaterialDetails findById(@PathVariable long id) {
        return materialService.findBy(id)
                .map(MaterialDetails::new)
                .orElseThrow(MaterialNotFoundException::new);
    }

    // GET http://localhost:8080/materials?name=Talavera
    @GetMapping(params = "name")
    MaterialDetails findByName(@RequestParam String name) {
        return materialService.findByName(name)
                .map(MaterialDetails::new)
                .orElseThrow(MaterialNotFoundException::new);
    }

}
