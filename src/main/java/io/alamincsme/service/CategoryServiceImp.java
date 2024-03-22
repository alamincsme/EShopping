package io.alamincsme.service;

import io.alamincsme.exception.APIException;
import io.alamincsme.exception.ResourceNotFoundException;
import io.alamincsme.model.Category;
import io.alamincsme.payload.CategoryDTO;
import io.alamincsme.payload.CategoryResponse;
import io.alamincsme.repository.CategoryRepo;
import io.alamincsme.repository.ProductRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImp implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ProductRepo productRepo;

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
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNo, pageSize, sortByAndOrder);
        Page<Category> pageCategories = categoryRepo.findAll(pageDetails);

        List<Category>  categories = pageCategories.getContent();

        if (categories.isEmpty()) {
            throw new APIException("Not category is created till now") ;
        }

        List<CategoryDTO> categoryDTOS = categories.stream()
                                                            .map((category -> modelMapper.
                                                                    map(category , CategoryDTO.class)))
                                                            .toList();



        var categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNo(pageCategories.getNumber());
        categoryResponse.setPageSize(pageCategories.getSize());
        categoryResponse.setTotalPages(pageCategories.getTotalPages());
        categoryResponse.setTotalElements(pageCategories.getTotalElements());
        categoryResponse.setLastPage(pageCategories.isLast());

        return categoryResponse ;
    }

    @Override
    public CategoryDTO updateCategory(Category category, Long categoryId) {
        Category saveCategory = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        saveCategory.setCategoryId(categoryId);
        saveCategory = categoryRepo.save(category);
        return modelMapper.map(saveCategory, CategoryDTO.class);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        var products = category.getProducts();

        products.forEach( product -> {
            productRepo.deleteById(product.getProductId());
        });

        categoryRepo.delete(category);

        return "Category With Name " + category.getCategoryName() + " deleted successfully";

    }
}
