package io.alamincsme.controller;

import io.alamincsme.payload.OrderDTO;
import io.alamincsme.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private  OrderService orderService;

    @PostMapping("/public/user/{emailId}/cart/{cartId}/payments/{paymentMethod}/order")
    public ResponseEntity<OrderDTO> processOrder(@PathVariable String emailId, @PathVariable Long cartId,@PathVariable String paymentMethod) {
        return new ResponseEntity<>(orderService.placeOrder(emailId, cartId, paymentMethod), HttpStatus.CREATED);
    }


    @GetMapping("/public/users/{emailId}/orders")
    public ResponseEntity<List<OrderDTO>> getOrderByUser(@PathVariable String emailId) {
        return new ResponseEntity<>(orderService.getOrdersByUser(emailId), HttpStatus.FOUND);
    }

    @GetMapping("/public/users/{emailId}/orders/{orderId}")
   public ResponseEntity<OrderDTO> getOrderUser(@PathVariable String emailId, @PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.getOrder(emailId, orderId), HttpStatus.FOUND);
    }





}
