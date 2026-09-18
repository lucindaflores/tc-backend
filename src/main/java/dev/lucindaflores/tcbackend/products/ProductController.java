package dev.lucindaflores.tcbackend.products;

import dev.lucindaflores.tcbackend.materials.Material;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("products")
@CrossOrigin
class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    /* DTO(s) */
    private record ProductName(String name) {
        ProductName(Product product) {
            this(product.getName());
        }
    }

    private record ProductCondensed(
            long id,
            String name,
            BigDecimal price,
            int stock,
            String imageUrl,
            String originName) {
        ProductCondensed(Product product) {
            this(product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getStock(),
                    product.getImageUrl(),
                    product.getOrigin().getName()
            );
        }
    }


    /* This DTO shows a product with all details for findById */
    private record ProductWithAllDetails(
            long id,
            String code,
            String name,
            String description,
            BigDecimal price,
            int stock,
            String imageUrl,
            long categoryId,
            String categoryName,
            long originId,
            String originName,
            Set<MaterialDetails> materialSet) {
        ProductWithAllDetails(Product product) {
            this(product.getId(),
                    product.getCode(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStock(),
                    product.getImageUrl(),

                    product.getCategory().getId(),
                    product.getCategory().getName(),

                    product.getOrigin().getId(),
                    product.getOrigin().getName(),

                    product.getMaterials()
                            .stream()
                            .map(MaterialDetails::new)
                            .collect(Collectors.toSet())
            );
        }
    }

    private record MaterialDetails(long id, String name) {
        MaterialDetails(Material material) {
            this(material.getId(), material.getName());
        }
    }

    /* Requests */
    // GET http://localhost:8080/products/count
    @GetMapping("count")
    long findCount() { return productService.findCount(); }

    // GET http://localhost:8080/products/all
    @GetMapping()
    List<ProductCondensed> findAll() {
        return productService.findAll()
                .stream()
                .map(ProductCondensed::new)
                .toList();
    }

    // GET http://localhost:8080/products/all
    @GetMapping("names")
    List<ProductName> findAllNames() {
        return productService.findAll()
                .stream()
                .map(ProductName::new)
                .toList();
    }

    // GET http://localhost:8080/products/{{id}}
    @GetMapping("{id}")
    ProductWithAllDetails findById(@PathVariable long id) {
        return productService.findById(id)
                // .stream()
                .map(ProductWithAllDetails::new)
                // .toList();
                .orElseThrow(ProductNotFoundException::new);
    }

    // GET http://localhost:8080/products?categoryId=1
    @GetMapping(params = "categoryId")
    List<ProductCondensed> findByCategoryId(@RequestParam long categoryId) {
        return productService.findByCategoryId(categoryId)
                .stream()
                .map(ProductCondensed::new)
                .toList();
    }


// GET http://localhost:8080/products?originId=2
    @GetMapping(params = "originId")
    List<ProductCondensed> findByOriginId(@RequestParam long originId) {
        return productService.findByOriginId(originId)
                .stream()
                .map(ProductCondensed::new)
                .toList();
    }

    // GET http://localhost:8080/products?materialId=4
    @GetMapping(params = "materialId")
    List<ProductCondensed> findByMaterialId(@RequestParam long materialId) {
        return productService.findByMaterialId(materialId)
                .stream()
                .map(ProductCondensed::new)
                .toList();
    }


    // GET http://localhost:8080/products/bymaterials?materialIds=1,2,3
    @GetMapping("byMaterials")
    List<ProductCondensed> findByMaterialIds(@RequestParam Set<Long> materialIds) {
        return productService.findByMaterialIds(materialIds)
                .stream()
                .map(ProductCondensed::new)
                .toList();
    }


}
