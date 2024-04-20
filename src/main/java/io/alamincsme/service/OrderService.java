package io.alamincsme.service;

import io.alamincsme.payload.OrderDTO;

import java.util.List;

public interface OrderService {
    OrderDTO placeOrder(String emailId, Long cartId, String paymentMethod);
    List<OrderDTO> getOrdersByUser(String emailId);
    OrderDTO getOrder(String emailId, Long orderId);


}
