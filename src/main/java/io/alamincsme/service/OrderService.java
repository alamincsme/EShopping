package io.alamincsme.service;

import io.alamincsme.payload.OrderDTO;
import io.alamincsme.payload.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderDTO placeOrder(String emailId, Long cartId, String paymentMethod);
    List<OrderDTO> getOrdersByUser(String emailId);
    OrderDTO getOrder(String emailId, Long orderId);
    OrderDTO updateOrder(String emailId, Long orderId, String orderStatus);
    OrderResponse getAllOrders(Integer pageNo, Integer pageSize, String sortBy, String sortOrder);


}
