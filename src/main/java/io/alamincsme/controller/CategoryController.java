package io.alamincsme.controller;

import io.alamincsme.config.AppConstants;
import io.alamincsme.model.Category;
import io.alamincsme.payload.CategoryDTO;
import io.alamincsme.payload.CategoryResponse;
import io.alamincsme.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNo", defaultValue = AppConstants.PAGE_NUMBER, required = true) Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = true) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

            var categoryResponse = categoryService.getCategories(pageNo, pageSize, sortBy, sortOrder);
            return new ResponseEntity<>(categoryResponse, HttpStatus.FOUND);

    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategoryByCategoryId(@RequestBody Category category, @PathVariable Long categoryId) {
        CategoryDTO categoryDTO = categoryService.updateCategory(category, categoryId);
        return new  ResponseEntity<CategoryDTO> (categoryDTO, HttpStatus.OK);
    }


    @DeleteMapping("/admin/{categoryId}")
    public ResponseEntity<String> deleteCategoryByCategoryId(@PathVariable Long categoryId) {
        String status = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
