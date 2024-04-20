package io.alamincsme.service;

import io.alamincsme.payload.OrderDTO;

public interface OrderService {
    OrderDTO placeOrder(String emailId, Long cartId, String paymentMethod);
}
