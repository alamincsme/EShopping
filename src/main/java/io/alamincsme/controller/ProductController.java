package io.alamincsme.controller;

import io.alamincsme.config.AppConstants;
import io.alamincsme.model.Product;
import io.alamincsme.payload.ProductDTO;
import io.alamincsme.payload.ProductResponse;
import io.alamincsme.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;
    private Integer pageNo;
    private Integer pageSize;
    private String sortBy;
    private String sortOrder;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody Product product, @PathVariable Long categoryId) {


        ProductDTO savedProduct = productService.addProduct(categoryId, product);

        return new ResponseEntity<ProductDTO>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNo", defaultValue = AppConstants.PAGE_NUMBER, required = true) Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = true) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = true) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = true) String sortOrder) {

        ProductResponse productResponse = productService.getAllProduct(pageNo, pageSize, sortBy, sortOrder);

        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.FOUND);
    }


    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductByCategory(
            @PathVariable(name = "categoryId") Long categoryId,
            @RequestParam(name = "pageNo", defaultValue = AppConstants.PAGE_NUMBER, required = true) Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = true) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = true) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = true) String sortOrder) {

        ProductResponse productResponse = productService.searchByCategory(categoryId, pageNo, pageSize, sortBy, sortOrder);

        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.FOUND);

    }


    @GetMapping("/public/{keyword}")
    public ResponseEntity<ProductResponse> getProductByKeyword(
            @PathVariable(name = "keyword") String word,
            @RequestParam(name = "pageNo", defaultValue = AppConstants.PAGE_NUMBER, required = true) Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = true) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = true) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = true) String sortOrder) {

        ProductResponse productResponse = productService.searchProductByKeyword(word, pageNo, pageSize, sortBy, sortOrder);

        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.FOUND);

    }


    @PostMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody Product product, @PathVariable Long productId) {
        ProductDTO updateProduct = productService.updateProduct(productId, product);
        return new  ResponseEntity<ProductDTO> (updateProduct, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<String> deleteProductByProductId(@PathVariable Long productId) {
        var status = productService.deleteProduct(productId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }


}
