package com.sunil.TrendNest.repo;

import com.sunil.TrendNest.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart,Long> {
    List<Cart> findByUserId(Long userId);

    Cart findByUserIdAndProductId(Long id, Long productId);

    Optional<Cart> findByIdAndProductId(Long id, Long productId);
}
