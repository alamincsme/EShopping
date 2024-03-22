package io.alamincsme.service;

import io.alamincsme.model.Category;
import io.alamincsme.payload.CategoryDTO;
import io.alamincsme.payload.CategoryResponse;

public interface CategoryService {

    public CategoryDTO createCategory(Category category);
    public CategoryResponse getCategories(Integer pageNo, Integer pageSize, String sortBy, String sortOrder);
    public CategoryDTO updateCategory(Category category, Long categoryId);
    public String deleteCategory(Long categoryId);


}
