package io.alamincsme.controller;

import io.alamincsme.payload.CartDTO;
import io.alamincsme.payload.ProductDTO;
import io.alamincsme.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartService cartService ;

//    @PostMapping("/public/carts/{cartId}/products/{productId}/quantity/{quantity}")
//    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long cartId,
//                                                    @PathVariable Long productId,
//                                                    @PathVariable Integer quantity){
//
//        var cartDTO = cartService.addProductToCart(cartId, productId, quantity);
//
//        return new  ResponseEntity<CartDTO> (cartDTO,HttpStatus.CREATED);
//
//    }


//    @GetMapping("/admin/carts")
//    public ResponseEntity<List<CartDTO>> getCarts() {
//        var cartDTOs = cartService.getAllCarts();
//        return new ResponseEntity<List<CartDTO>> (cartDTOs , HttpStatus.FOUND);
//    }
//
//    @GetMapping("/public/users/{emailId}/carts/{cartId}")
//    public ResponseEntity<CartDTO> getCart(@PathVariable String emailId , @PathVariable Long cartId) {
//        return new  ResponseEntity<CartDTO> (cartService.getCart(emailId, cartId), HttpStatus.FOUND);
//    }


    @PutMapping("/public/carts/{cartId}/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long cartId, @PathVariable Long productId, @PathVariable Integer quantity) {
        CartDTO cartDTO = cartService.updateProductQuantityInCart(cartId, productId, quantity);

        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
    }

    @DeleteMapping("/public/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
        return new ResponseEntity<>(cartService.deleteProductFromCart(cartId, productId), HttpStatus.OK);
    }
}
