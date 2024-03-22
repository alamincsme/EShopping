package io.alamincsme.controller;

import io.alamincsme.model.Category;
import io.alamincsme.payload.CategoryDTO;
import io.alamincsme.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/admin/category")
    public ResponseEntity<CategoryDTO> createCategory(@Valid  @RequestBody Category category) {
        CategoryDTO categoryDTO = categoryService.createCategory(category);
        return new ResponseEntity<CategoryDTO>(categoryDTO, HttpStatus.CREATED);
    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategoryByCategoryId(@RequestBody Category category, @PathVariable Long categoryId) {
        CategoryDTO categoryDTO = categoryService.updateCategory(category, categoryId);
        return new  ResponseEntity<CategoryDTO> (categoryDTO, HttpStatus.OK);
    }
}
