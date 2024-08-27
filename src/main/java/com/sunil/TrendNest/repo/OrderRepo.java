package com.sunil.TrendNest.repo;

import com.sunil.TrendNest.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Orders,Long> {
    List<Orders> findByUserId(Long id);
}
