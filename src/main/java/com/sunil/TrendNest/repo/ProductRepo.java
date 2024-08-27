package com.sunil.TrendNest.repo;

import com.sunil.TrendNest.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {
    @Query("SELECT p from Product p where "+
            "LOWER(p.name) LIKE CONCAT('%', :keyword, '%') OR " +
            "LOWER(p.description) LIKE CONCAT('%', :keyword, '%') OR "+
            "LOWER(p.category) LIKE CONCAT('%', :keyword, '%') OR " +
            "LOWER(p.brand) LIKE CONCAT('%', :keyword, '%')")
    List<Product> searchProducts(String keyword);
}