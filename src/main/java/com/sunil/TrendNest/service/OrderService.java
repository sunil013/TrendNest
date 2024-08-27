package com.sunil.TrendNest.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.sunil.TrendNest.DTOs.OrderItemDTO;
import com.sunil.TrendNest.DTOs.PaymentSuccessDTO;
import com.sunil.TrendNest.Utils.JwtUtil;
import com.sunil.TrendNest.model.OrderItem;
import com.sunil.TrendNest.model.Orders;
import com.sunil.TrendNest.model.TransactionDetails;
import com.sunil.TrendNest.model.Users;
import com.sunil.TrendNest.repo.OrderItemRepo;
import com.sunil.TrendNest.repo.OrderRepo;
import com.sunil.TrendNest.repo.UserRepo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private static final String KEY="rzp_test_KPZPD8evVFWOt3";
    private static final String KEY_SECRET="Mbjd5t725mTrpx7xfihHuOwD";
    private static final String CURRENCY="INR";

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;

    public List<Orders> getUserOrders(String token) {
        String username = jwtService.extractUserName(JwtUtil.extractJwtToken(token));
        Users user = userRepo.findByUsername(username);
        return orderRepo.findByUserId(user.getId());
    }

    public TransactionDetails createTransaction(Double amount) {
        try{
            JSONObject json = new JSONObject();
            json.put("amount",(amount * 100));
            json.put("currency",CURRENCY);

            RazorpayClient razorpayClient = new RazorpayClient(KEY,KEY_SECRET);
            Order order = razorpayClient.orders.create(json);
            return prepareTransactionDetails(order);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public TransactionDetails prepareTransactionDetails(Order order){
        String orderId = order.get("id");
        Integer amount = order.get("amount");
        String currency = order.get("currency");
        return new TransactionDetails(orderId,amount,currency,KEY);
    }

    public void saveOrder(PaymentSuccessDTO paymentSuccessDTO, List<OrderItemDTO> orderItemDTOList, String token) {
        String username = jwtService.extractUserName(JwtUtil.extractJwtToken(token));
        Users user = userRepo.findByUsername(username);

        Orders orders = new Orders();
        orders.setUser(user);
        orders.setTotalAmount(paymentSuccessDTO.getTotalAmount());
        orders.setOrderDate(LocalDateTime.now());
        orders.setStatus("PAID");
        orders.setPaymentMethod(paymentSuccessDTO.getPaymentMethod());
        orders.setShippingAddress("");

        orders = orderRepo.save(orders);

        // Create and save OrderItem entities
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDTO itemDTO : orderItemDTOList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(orders);
            orderItem.setProduct(itemDTO.getProduct());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(itemDTO.getPrice());
            orderItems.add(orderItem);
        }

        orderItemRepo.saveAll(orderItems);
    }
}
