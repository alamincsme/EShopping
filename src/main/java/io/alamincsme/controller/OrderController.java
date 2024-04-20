package io.alamincsme.controller;

import io.alamincsme.payload.OrderDTO;
import io.alamincsme.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private  OrderService orderService;

    @PostMapping("/public/user/{emailId}/cart/{cartId}/payments/{paymentMethod}/order")
    public ResponseEntity<OrderDTO> processOrder(@PathVariable String emailId, @PathVariable Long cartId,@PathVariable String paymentMethod) {
        return new ResponseEntity<>(orderService.placeOrder(emailId, cartId, paymentMethod), HttpStatus.CREATED);
    }







}
