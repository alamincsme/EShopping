package io.alamincsme.service.service_impl;

import io.alamincsme.model.Product;
import io.alamincsme.repository.ProductRepository;
import io.alamincsme.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repo;
    @Override
    public List<Product> findAllProductsSortedByName() {
        return (List<Product>) repo.findAll()
                .stream()
                .sorted(Comparator.comparing(Product::getName))
                .collect(Collectors.toList());
    }
}
