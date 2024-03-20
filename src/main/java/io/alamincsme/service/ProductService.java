package io.alamincsme.service;

import io.alamincsme.model.Product;
import io.alamincsme.payload.ProductDTO;
import io.alamincsme.payload.ProductResponse;

public interface ProductService {

    ProductDTO addProduct(Long categoryId, Product product);
    ProductResponse getAllProduct(Integer pageNo, Integer pageSize, String sortBy, String sortOrder);
    ProductResponse searchByCategory(Long categoryId, Integer pageNo, Integer pageSize, String sortBy, String sortOrder);
    ProductDTO updateProduct(Long productId, Product product) ;
    ProductResponse searchProductByKeyword (String keyword , Integer pageNo, Integer pageSize, String sortBy, String sortOrder);
    String deleteProduct (Long productId);

}
