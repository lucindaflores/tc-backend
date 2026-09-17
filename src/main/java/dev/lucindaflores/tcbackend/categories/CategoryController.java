package dev.lucindaflores.tcbackend.categories;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("categories")
@CrossOrigin
class CategoryController {

    private final CategoryService categoryService;

    CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /* DTO(s) */
    private record CategoryName(long id, String name) {
        CategoryName(Category category) {
            this(category.getId(), category.getName());
        }
    }

    /* Requests */
    // GET http://localhost:8080/categories
    @GetMapping()
    List<CategoryName> findAll() {
        return categoryService.findAll()
                .stream()
                .map(CategoryName::new)
                .toList();
    }

}
