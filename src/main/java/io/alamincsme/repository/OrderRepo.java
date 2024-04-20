package io.alamincsme.repository;

import io.alamincsme.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findAllByEmail(String emailId);

    @Query("SELECT o FROM Order  o WHERE o.email=?1 AND o.orderId=?2")
    Order findOrderByEmailAndOrderId(String emailId, Long orderId);
}
