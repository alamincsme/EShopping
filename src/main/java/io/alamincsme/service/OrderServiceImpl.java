package io.alamincsme.service;

import io.alamincsme.exception.APIException;
import io.alamincsme.exception.ResourceNotFoundException;
import io.alamincsme.model.*;
import io.alamincsme.payload.OrderDTO;
import io.alamincsme.payload.OrderItemDTO;
import io.alamincsme.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;
    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final PaymentRepo paymentRepo;

    private final CartService cartService ;
    private final ProductService productService;
    private  final ModelMapper modelMapper ;


    public OrderServiceImpl(CartRepo cartRepo, CartItemRepo cartItemRepo, OrderRepo orderRepo, OrderItemRepo orderItemRepo, PaymentRepo paymentRepo, CartService cartService, ProductService productService, ModelMapper modelMapper) {
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.paymentRepo = paymentRepo;
        this.cartService = cartService;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderDTO placeOrder(String emailId, Long cartId, String paymentMethod) {
        Cart cart = cartRepo.findCartByEmailAndCartId(emailId, cartId);
        if (cart == null) {
            throw  new ResourceNotFoundException("Cart", "Cartid", cartId);
        }

        Order order = new Order();
        order.setEmail(emailId);
        order.setOrderDate(LocalDate.now());
        order.setOrderStatus("Order Accepted!");
        order.setTotalAmount(cart.getTotalPrice());

        Payment payment = new Payment();

        payment.setOrder(order);
        payment.setPaymentMethod(paymentMethod);

        paymentRepo.save(payment);

        order.setPayment(payment);
        Order saveOrder = orderRepo.save(order);

        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new APIException("Cart is empty");

        }

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem: cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrder(order);
            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
            orderItems.add(orderItem);

        }

        orderItemRepo.saveAll(orderItems);
        double totalPrice = cart.getTotalPrice();

        System.out.println("Size : " + cartItems.size());
        for (CartItem cartItem : cartItems) {
            System.out.println("Product price : " + cartItem.getProductPrice() );
            totalPrice -= cartItem.getProductPrice() * cartItem.getQuantity();
            System.out.println("Total Price : " + totalPrice);
        }


        cart.setTotalPrice(totalPrice);

        // Remove ordered items from the cart
        cart.getCartItems().clear();

        // Update the cart's total price in the database
        System.out.println("cart price : " + cart.getTotalPrice());



//        cart.getCartItems().forEach(item -> {
//            int quantity = item.getQuantity();
//            Product product = item.getProduct();
//
//            // Remove item from the cart
//            cartService.deleteProductFromCart(cartId, item.getProduct().getProductId());
//
//            // Update product quantity
//            product.setQuantity(product.getQuantity() - quantity);
//        });

        cart.getCartItems().forEach(item -> {
            int quantity = item.getQuantity();
            Product product = item.getProduct();

            // Logging for debugging
            System.out.println("Deleting item: " + item);

            // Remove item from the cart
            cartService.deleteProductFromCart(cartId, item.getProduct().getProductId());

            // Logging for debugging
            System.out.println("Updated product quantity: " + product.getQuantity());

            // Update product quantity
            product.setQuantity(product.getQuantity() - quantity);
        });

       // Logging for debugging
        System.out.println("Updated cart: " + cart);

       // Save changes to the cart (removed items and updated total price)
        cartRepo.save(cart);



        OrderDTO orderDTO = modelMapper.map(saveOrder, OrderDTO.class);
        orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)));

        return orderDTO;

    }


}
