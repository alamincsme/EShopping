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

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody Product product, @PathVariable Long categoryId) {


        ProductDTO savedProduct = productService.addProduct(categoryId, product);

        return new ResponseEntity<ProductDTO>(savedProduct, HttpStatus.CREATED);
    }


}
