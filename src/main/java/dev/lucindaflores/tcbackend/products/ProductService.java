package dev.lucindaflores.tcbackend.products;


import dev.lucindaflores.tcbackend.categories.CategoryRepository;
import dev.lucindaflores.tcbackend.materials.MaterialRepository;
import dev.lucindaflores.tcbackend.origins.OriginRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly=true)
class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OriginRepository originRepository;
    private final MaterialRepository materialRepository;

    ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, OriginRepository originRepository, MaterialRepository materialRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.originRepository = originRepository;
        this.materialRepository = materialRepository;
    }

    /* Methods */
    long findCount() {
        return productRepository.count();
    }

    List<Product> findAll() {
        return productRepository.findAll();
    }

    Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }

    List<Product> findByCategoryId(long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    List<Product> findByOriginId(long originId) {
        return  productRepository.findByOriginId(originId);
    }

    List<Product> findByMaterialId(long materialId) {
        return productRepository.findDistinctByMaterialsId(materialId);
    }

    List<Product> findByMaterialIds(Set<Long> materialIds) {
        return productRepository.findDistinctByMaterials_IdIn(materialIds);
    }

}
