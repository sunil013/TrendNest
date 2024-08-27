package com.sunil.TrendNest.controller;

import com.sunil.TrendNest.DTOs.ProductSummaryDTO;
import com.sunil.TrendNest.DTOs.ProductsSearchDTO;
import com.sunil.TrendNest.model.Product;
import com.sunil.TrendNest.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

    @Autowired
   private ProductService service;

    @GetMapping("/public/products")
    public ResponseEntity<List<ProductSummaryDTO>> getAllProducts() {
        List<ProductSummaryDTO> products = service.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping("/products/add/all")
    public ResponseEntity<String> addProducts(@RequestBody List<Product> products) {
        try {
            List<Product> products1 = service.addProducts(products);
            if (products1 != null) {
                return new ResponseEntity<>("Products added", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Failed to add some products", HttpStatus.PARTIAL_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add products", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/public/product/{id}")
    public ResponseEntity<Product> getProductDetails(@PathVariable Long id){
        Product product = service.getProductDetails(id);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<ProductsSearchDTO>> searchProducts(@RequestParam String keyword){
        List<ProductsSearchDTO> products = service.searchProducts(keyword);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }


}
