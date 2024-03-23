package io.alamincsme.service;

import io.alamincsme.exception.APIException;
import io.alamincsme.exception.ResourceNotFoundException;
import io.alamincsme.model.Category;
import io.alamincsme.model.Product;
import io.alamincsme.payload.ProductDTO;
import io.alamincsme.payload.ProductResponse;
import io.alamincsme.repository.CartRepo;
import io.alamincsme.repository.CategoryRepo;
import io.alamincsme.repository.ProductRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class ProductServiceImp implements ProductService {


    private  ProductRepo productRepo ;

    @Autowired
    private CategoryRepo categoryRepo ;

    @Autowired
    private CartRepo cartRepo ;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public ProductDTO addProduct(Long categoryId, Product product) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        boolean isProductNotPresent = true ;

        List<Product> products  = category.getProducts() ;

        for (Product value : products) {
            if (value.getProductName().equals(product.getProductName())
                    && value.getDescription().equals(product.getDescription())) {

                isProductNotPresent = false;
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

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable details = PageRequest.of(pageNo, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepo.findAll(details);
        List<Product> products = pageProducts.getContent() ;

        List<ProductDTO> productDTOS = products
                                            .stream()
                                            .map(product -> modelMapper.map(product , ProductDTO.class))
                                            .collect(Collectors.toList());


        ProductResponse productResponse = new ProductResponse();
        return getProductResponse(pageProducts, productDTOS, productResponse);
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId, Integer pageNo, Integer pageSize, String sortBy, String sortOrder) {
       Category category = categoryRepo.findById(categoryId)
               .orElseThrow(() -> new  ResourceNotFoundException("Category", "CategoryId", categoryId));



       Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending() ;
       Pageable pageDetails = PageRequest.of(pageNo, pageSize, sortByAndOrder);

       //product search by category.
       var pageProducts  = productRepo.findByCategory(category, pageDetails);
        System.out.println(pageProducts);
       var products = pageProducts.getContent();

       if (products.isEmpty()) {
           throw  new APIException("Category doesn't contain any product!");
       }

        List<ProductDTO> productDTOS = products
                .stream()
                .map(product -> modelMapper.map(product , ProductDTO.class))
                .collect(Collectors.toList());

       var productResponse = new ProductResponse();
       return getProductResponse(pageProducts, productDTOS, productResponse);

    }

    @Override
    public ProductDTO updateProduct(Long productId, Product product) {
        Product productFromDB = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        if (productFromDB == null) {
            throw new APIException("Product not found with productId " + productId);
        }

        product.setProductId(productId);
        product.setCategory(productFromDB.getCategory());

        double specialPrice = product.getPrice() - (product.getDiscount() * .01) * product.getPrice() ;
        productFromDB.setSpecialPrice(specialPrice);

        Product saveProduct = productRepo.save(product);

        return modelMapper.map(product, ProductDTO.class);

    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword, Integer pageNo, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNo,pageSize,sortAndOrder);
        var pageProducts = productRepo.findByProductNameLike("%" + keyword + "%" , pageDetails);

        var products = pageProducts.getContent() ;

        if (products.isEmpty()) {
            throw  new APIException(keyword + " product not found!!");
        }

        var productsDTOS = products
                .stream()
                .map((product) -> modelMapper.map(product , ProductDTO.class))
                .toList();
        var productResponse = new ProductResponse();
        return getProductResponse(pageProducts, productsDTOS, productResponse);
    }

    @Override
    public String deleteProduct(Long productId) {
        System.out.println("this is method");
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));


        productRepo.delete(product);

        return "Product with productId " + product + " deleted successfully";

    }

//    @Override
//    public String deleteProduct(Long productId) {
//
//        Product product = productRepo.findById(productId)
//                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
//
//        productRepo.delete(product);
//
//        return "Product with productId: " + productId + " deleted successfully !!!";
//    }


    private ProductResponse getProductResponse(Page<Product> pageProducts, List<ProductDTO> productDTOS, ProductResponse productResponse) {
        productResponse.setContent(productDTOS);
        productResponse.setPageNo(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse ;
    }
}
