package io.alamincsme.controller;

import io.alamincsme.config.AppConstants;
import io.alamincsme.payload.OrderDTO;
import io.alamincsme.payload.OrderResponse;
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


    @PutMapping("/admin/users/{emailId}/orders/{orderId}/orderStatus/{orderStatus}")
    public ResponseEntity<OrderDTO> updateOrder(
            @PathVariable String emailId, @PathVariable Long orderId,
            @PathVariable String orderStatus)  {
        return new ResponseEntity<>(orderService.updateOrder(emailId, orderId, orderStatus), HttpStatus.OK);
    }


    @GetMapping("/admin/orders")
    public ResponseEntity<OrderResponse> getAllOrders(
        @RequestParam(name = "pageNo", defaultValue = AppConstants.PAGE_NUMBER, required = true) int pageNo,
        @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = true) int pageSize,
        @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ORDER_BY, required = true) String sortBy,
        @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = true) String sortOrder) {

        return new ResponseEntity<>(orderService.getAllOrders(pageNo, pageSize, sortBy, sortOrder), HttpStatus.FOUND);
    }
}
