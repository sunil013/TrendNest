package com.sunil.TrendNest.controller;

import com.sunil.TrendNest.Constants.CartServiceMessages;
import com.sunil.TrendNest.DTOs.CartAddProductDTO;
import com.sunil.TrendNest.DTOs.CartUpdateQuantityDTO;
import com.sunil.TrendNest.model.Cart;
import com.sunil.TrendNest.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService service;

    @GetMapping("/user")
    public ResponseEntity<List<Cart>> getCartItems(@RequestHeader("Authorization") String token) {
        try {
            List<Cart> cartItems = service.getCartItemsForUser(token);
            return new ResponseEntity<>(cartItems, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addProductToCart(@RequestBody CartAddProductDTO product, @RequestHeader("Authorization") String token){
        return new ResponseEntity<>(service.addProductToCart(product,token),HttpStatus.OK);
    }

    @PostMapping("/product/update")
    public ResponseEntity<String> updateQuantity(@RequestBody CartUpdateQuantityDTO product){
      String response = service.updateQuantity(product);
      if (response.equals(CartServiceMessages.CART_ITEM_NOT_FOUND)){
          return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
      }
       return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/product/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id,@RequestHeader("Authorization") String token){
        String response = service.deleteProduct(id,token);
        if (response.equals(CartServiceMessages.PRODUCT_REMOVED)){
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}
