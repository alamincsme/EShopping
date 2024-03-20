package io.alamincsme.service;

import io.alamincsme.exception.APIException;
import io.alamincsme.exception.ResourceNotFoundException;
import io.alamincsme.model.Category;
import io.alamincsme.model.Product;
import io.alamincsme.payload.ProductDTO;
import io.alamincsme.payload.ProductResponse;
import io.alamincsme.repository.CategoryRepo;
import io.alamincsme.repository.ProductRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Transactional
@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepo productRepo ;

    @Autowired
    private CategoryRepo categoryRepo ;

    @Autowired
    private ModelMapper modelMapper;



    @Override
    public ProductDTO addProduct(Long categoryId, Product product) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        boolean isProductNotPresent = true ;

        List<Product> products  = category.getProducts() ;

        for (int i = 0 ; i < products.size() ; ++i) {
            if (products.get(i).getProductName().equals(product.getProductName())
                        && products.get(i).getDescription().equals(product.getDescription())) {

                isProductNotPresent = false ;
                break;

            }
        }

        if (isProductNotPresent) {
            product.setCategory(category);
            double specialPrice = product.getPrice() - ((product.getDiscount() * .01)) * product.getPrice() ;
            product.setSpecialPrice(specialPrice);

            Product saveProduct = productRepo.save(product);

            return modelMapper.map(saveProduct, ProductDTO.class);

        } else {
            throw  new APIException("Product Already Exists!!!");
        }
    }

    @Override
    public ProductResponse getAllProduct(Integer pageNo, Integer pageSize, String sortBy, String sortOrder) {
        return null;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId, Integer pageNo, Integer pageSize, String sortBy, String sortOrder) {
        return null;
    }

    @Override
    public ProductDTO updateProduct(Long productId, Product product) {
        return null;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword, Integer pageNo, Integer pageSize, String sortBy, String sortOrder) {
        return null;
    }

    @Override
    public String deleteProduct(Long productId) {
        return null;
    }
}
