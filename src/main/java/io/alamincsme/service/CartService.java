package io.alamincsme.service;

import com.mysql.cj.log.Log;
import io.alamincsme.payload.CartDTO;

import java.util.List;

public interface CartService {
    CartDTO addProductToCart(Long cartId, Long productId, Integer quantity);
    List<CartDTO> getAllCarts();
    CartDTO getCart(String emailId, Long cartId) ;
    CartDTO updateProductQuantityInCart(Long cartId, Long productId, Integer quantity) ;
    void updateProductInCarts(Long cartId, Long productId) ;
    String deleteProductFromCart(Long cartid, Long productId);


}
