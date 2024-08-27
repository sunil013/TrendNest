package com.sunil.TrendNest.DTOs;

import com.sunil.TrendNest.model.Product;

public class OrderItemDTO {
    private Product product;
    private Integer quantity;
    private Double price;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
