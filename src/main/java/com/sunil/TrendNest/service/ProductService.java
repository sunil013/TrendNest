package com.sunil.TrendNest.service;

import com.sunil.TrendNest.DTOs.ProductSummaryDTO;
import com.sunil.TrendNest.DTOs.ProductsSearchDTO;
import com.sunil.TrendNest.model.Product;
import com.sunil.TrendNest.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepo repo;

    public List<ProductSummaryDTO> getAllProducts() {
        return repo.findAll().stream()
                .map(product -> new ProductSummaryDTO(
                        product.getId(),
                        product.getName(),
                        product.getCategory(),
                        product.getPrice(),
                        product.getRating(),
                        product.getBrand(),
                        product.isAvailability(),
                        product.getImageUrl()
                ))
                .collect(Collectors.toList());
    }


    public List<Product> addProducts(List<Product> products) {
        return repo.saveAll(products);
    }

    public Product getProductDetails(Long id) {
        return repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    public List<ProductsSearchDTO> searchProducts(String keyword) {
       return repo.searchProducts(keyword).stream()
               .map(product -> new ProductsSearchDTO(
                       product.getId(),
                       product.getName(),
                       product.getImageUrl()))
                       .collect(Collectors.toList());
    }
}