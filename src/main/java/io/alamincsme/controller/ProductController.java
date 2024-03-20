package io.alamincsme.controller;

import io.alamincsme.model.Product;
import io.alamincsme.payload.ProductDTO;
import io.alamincsme.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct (@Valid @RequestBody Product product, @PathVariable Long categoryId) {
        ProductDTO saveProduct = productService.addProduct(categoryId, product);
        return new ResponseEntity<ProductDTO>(saveProduct, HttpStatus.CREATED);
    }
}
