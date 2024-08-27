package com.sunil.TrendNest.repo;

import com.sunil.TrendNest.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepo extends JpaRepository<OrderItem,Long> {
}
