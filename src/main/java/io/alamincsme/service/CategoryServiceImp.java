package io.alamincsme.service;

import io.alamincsme.exception.APIException;
import io.alamincsme.model.Category;
import io.alamincsme.payload.CategoryDTO;
import io.alamincsme.payload.CategoryResponse;
import io.alamincsme.repository.CategoryRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImp implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper;



    @Override
    public CategoryDTO createCategory(Category category) {
        Category saveCategory = categoryRepo.findByCategoryName(category.getCategoryName());

        if (saveCategory != null) {
            throw new APIException("Category with name " + category.getCategoryName() + " already exist !!!");
        }

        saveCategory = categoryRepo.save(category);
        return modelMapper.map(saveCategory, CategoryDTO.class);
    }


    @Override
    public CategoryResponse getCategories(Integer pageNo, Integer pageSize, String sortBy, String sortOrder) {
        return null;
    }

    @Override
    public CategoryDTO updateCategory(Category category, Long categoryId) {
        return null;
    }

    @Override
    public String deleteCategory(Long categoryId) {
        return null;
    }
}
