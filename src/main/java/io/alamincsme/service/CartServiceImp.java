package io.alamincsme.service;

import io.alamincsme.exception.APIException;
import io.alamincsme.exception.ResourceNotFoundException;
import io.alamincsme.model.Cart;
import io.alamincsme.model.CartItem;
import io.alamincsme.model.Product;
import io.alamincsme.payload.CartDTO;
import io.alamincsme.payload.CartItemDTO;
import io.alamincsme.payload.ProductDTO;
import io.alamincsme.repository.CartItemRepo;
import io.alamincsme.repository.CartRepo;
import io.alamincsme.repository.ProductRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImp implements CartService {

    private final CartRepo cartRepo ;
    private final CartItemRepo cartItemRepo;
    private final ProductRepo productRepo;
    private final  ModelMapper modelMapper;


    public CartServiceImp(CartRepo cartRepo, CartItemRepo cartItemRepo, ProductRepo productRepo, ModelMapper modelMapper) {
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
        this.productRepo = productRepo;
        this.modelMapper = modelMapper;
    }


    @Override
    public CartDTO addProductToCart(Long cartId, Long productId, Integer quantity) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));


        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem != null) {
            throw new APIException("Product " + product.getProductName() + " already exists in the cart");
        }

        if (product.getQuantity() == 0) {
            throw new APIException(product.getProducts() + " is not available");
        }

        if (product.getQuantity() < quantity) {
            throw  new APIException("Please! make an order of the " + product.getProductName() + " less than or equal ot the quantity " + product.getQuantity());
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        cartItemRepo.save(newCartItem);

        product.setQuantity(product.getQuantity() - quantity);

        cart.setTotalPrice(cart.getTotalPrice()  + product.getSpecialPrice() * quantity);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<ProductDTO> productDTOS = cart.getCartItems()
                                            .stream()
                                            .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class))
                                            .collect(Collectors.toList());

//        for (ProductDTO productDTO : productDTOS) System.out.println(productDTO.getDescription());
        cartDTO.setProducts(productDTOS);
        return cartDTO ;

    }


    @Override
    public List<CartDTO> getAllCarts() {

        List<Cart> carts = cartRepo.findAll();

        if (carts.isEmpty()) {
            throw new APIException("No cart exists");
        }

        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

            List<ProductDTO> products = cart.getCartItems().stream()
                    .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

            cartDTO.setProducts(products);

            return cartDTO;

        }).collect(Collectors.toList());

        return cartDTOs;
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart = cartRepo.findCartByEmailAndCartId(emailId, cartId);

        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        var products = cart
                        .getCartItems()
                        .stream()
                        .map((p) -> modelMapper.map(p.getProduct(), ProductDTO.class))
                        .toList();

        cartDTO.setProducts(products);

        return cartDTO ;

    }

//    @Override
//    public CartDTO updateProductQuantityInCart(Long cartId, Long productId, Integer quantity) {
//
//        if (quantity == 0) {
//            throw new APIException("Please enter a value greater than or equal to 1");
//        }
//
//        Cart cart = cartRepo.findById(cartId)
//                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
//
//        Product product = productRepo.findById(productId)
//                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
//
//
//        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);
//
////        if (cartItem == null) {
////            throw new APIException("Product " + product.getProductName() + " not available in the cart!!!");
////        } else if (product.getQuantity() <= quantity) {
////            throw new APIException("Please, make an order of the " + product.getProductName()
////                    + " less than or equal to the quantity " + product.getQuantity() + ".");
////        }
//        int totalQuantity = 0 ;
//
//        if (product.getQuantity() >= cartItem.getQuantity() + quantity && quantity == 1) {
//            totalQuantity = cartItem.getQuantity() + quantity ;
//            //complete
//        } else if (cartItem.getQuantity() - quantity != 0 && quantity == - 1) {
//            //complete
//        }
//
//        double totalProductPrice = product.getSpecialPrice() * quantity ;
//        System.out.println(totalProductPrice);
//        double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice());
//        System.out.println(cartPrice);
//
////
////        product.setQuantity(product.getQuantity() + cartItem.getQuantity() - quantity);
////
//        cartItem.setProductPrice(totalProductPrice);
//        cartItem.setDiscount(cartItem.getDiscount());
//        cartItem.setQuantity(quantity);
//
//
//        cart.setTotalPrice(cartPrice + (product.getSpecialPrice() * quantity));
////
//        cartItem = cartItemRepo.save(cartItem);
//
//        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
//
//       var productDTOs  = cart.getCartItems().stream()
//                .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());
//
//
//        cartDTO.setProducts(productDTOs);
//        return cartDTO ;
//
//    }
//
@Override
public CartDTO updateProductQuantityInCart(Long cartId, Long productId, Integer quantity) {

    if (quantity == 0) {
        throw new APIException("Please enter a value greater than or equal to 1");
    }

    Cart cart = cartRepo.findById(cartId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

    Product product = productRepo.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

    CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);

    int updatedQuantity;
    if (quantity == 1) {
        // Increment quantity by one
        updatedQuantity = cartItem.getQuantity() + 1;
    } else if (quantity == -1) {
        // Decrement quantity by one
        updatedQuantity = cartItem.getQuantity() - 1;
    } else {
        // Set quantity directly
        updatedQuantity = quantity;
    }

    if (updatedQuantity < 1 || updatedQuantity > product.getQuantity()) {
        throw new APIException("Invalid quantity. Please enter a valid quantity.");
    }

    double totalProductPrice = product.getSpecialPrice() * updatedQuantity;

    // Calculate the difference in the cart price
    double cartPriceDifference = totalProductPrice - cartItem.getProductPrice();

    // Update cart total price
    double cartTotalPrice = cart.getTotalPrice() + cartPriceDifference;

    // Update cart item with new quantity and price
    cartItem.setQuantity(updatedQuantity);
    cartItem.setProductPrice(totalProductPrice);

    // Save the updated cart item
    cartItem = cartItemRepo.save(cartItem);

    // Update cart total price
    cart.setTotalPrice(cartTotalPrice);

    // Save the updated cart
    cart = cartRepo.save(cart);

    // Map the updated cart to DTO
    CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

    // Map the products in the cart to DTOs
    List<ProductDTO> productDTOs = cart.getCartItems().stream()
            .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class))
            .collect(Collectors.toList());

    cartDTO.setProducts(productDTOs);
    return cartDTO;
}


    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem == null) {
            throw new ResourceNotFoundException("Product", "productId", productId);
        }

        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));

        Product product = cartItem.getProduct();
        product.setQuantity(product.getQuantity() + cartItem.getQuantity());

        cartItemRepo.deleteCartItemByProductIdAndCartId(cartId, productId);

        return "Product " + cartItem.getProduct().getProductName() + " removed from the cart !!!";
    }
}
