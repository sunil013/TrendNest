package com.sunil.TrendNest.service;

import com.sunil.TrendNest.Constants.CartServiceMessages;
import com.sunil.TrendNest.DTOs.CartAddProductDTO;
import com.sunil.TrendNest.DTOs.CartUpdateQuantityDTO;
import com.sunil.TrendNest.Utils.JwtUtil;
import com.sunil.TrendNest.model.Cart;
import com.sunil.TrendNest.model.Users;
import com.sunil.TrendNest.repo.CartRepo;
import com.sunil.TrendNest.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepo repo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTService jwtService;

    public List<Cart> getCartItemsForUser(String token) throws Exception {
        String username = jwtService.extractUserName(JwtUtil.extractJwtToken(token));
        Users user = userRepo.findByUsername(username);
        return repo.findByUserId(user.getId());
    }

    public String addProductToCart(CartAddProductDTO product, String token) {
        String username = jwtService.extractUserName(JwtUtil.extractJwtToken(token));
        Users user = userRepo.findByUsername(username);

        Cart existingCart = repo.findByUserIdAndProductId(user.getId(),product.getProductId());

        if(existingCart != null){
            existingCart.setQuantity(existingCart.getQuantity() + product.getQuantity());
            existingCart.setModifiedAt(new Date());
            repo.save(existingCart);
        }else {
            Cart cart = new Cart();
            cart.setUserId(user.getId());
            cart.setProductId(product.getProductId());
            cart.setQuantity(product.getQuantity());
            cart.setCreatedAt(new Date());
            cart.setModifiedAt(new Date());
            repo.save(cart);
        }
        return CartServiceMessages.PRODUCT_ADDED;
    }


    public String updateQuantity(CartUpdateQuantityDTO product) {
        Optional<Cart> cartOptional = repo.findByIdAndProductId(product.getId(),product.getProductId());

        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            cart.setQuantity(product.getQuantity());
            cart.setModifiedAt(new Date());
            repo.save(cart);
            return CartServiceMessages.PRODUCT_UPDATED;
        }

        return CartServiceMessages.CART_ITEM_NOT_FOUND;
    }

    public String deleteProduct(Long id, String token) {
        String username = jwtService.extractUserName(JwtUtil.extractJwtToken(token));
        Users user = userRepo.findByUsername(username);
        Optional<Cart> cartOptional = repo.findById(id);

        if (cartOptional.isPresent()){
            Cart cart = cartOptional.get();
            if (cart.getUserId().equals(user.getId())){
                repo.deleteById(id);
                return CartServiceMessages.PRODUCT_REMOVED;
            } else {
                return CartServiceMessages.UNAUTHORIZED;
            }
        }

        return CartServiceMessages.PRODUCT_NOT_FOUND;
    }
}
