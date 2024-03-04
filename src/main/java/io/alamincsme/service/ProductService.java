package io.alamincsme.service;

import io.alamincsme.model.Product;

import java.util.List;

public interface ProductService {

    public List<Product> findAllProductsSortedByName() ;
}
