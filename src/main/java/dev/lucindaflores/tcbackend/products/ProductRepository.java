package dev.lucindaflores.tcbackend.products;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryId(long categoryId);

    List<Product> findByOriginId(long originId);

    List<Product> findDistinctByMaterialsId(long materialId);

    List<Product> findDistinctByMaterials_IdIn(Set<Long> materialIds);

}
