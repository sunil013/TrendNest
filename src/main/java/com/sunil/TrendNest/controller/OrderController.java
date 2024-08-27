package com.sunil.TrendNest.controller;

import com.sunil.TrendNest.DTOs.OrderItemDTO;
import com.sunil.TrendNest.DTOs.PaymentSuccessDTO;
import com.sunil.TrendNest.model.Orders;
import com.sunil.TrendNest.model.TransactionDetails;
import com.sunil.TrendNest.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService service;

    @GetMapping("/user")
    public ResponseEntity<List<Orders>> getUserOrders(@RequestHeader("Authorization") String token){
        return new ResponseEntity<>(service.getUserOrders(token), HttpStatus.OK);
    }

    @GetMapping("/payment/createTransaction/{amount}")
    public TransactionDetails createTransaction(@PathVariable Double amount){
        return service.createTransaction(amount);
    }

    @PostMapping("/payment/success")
    public void saveTransactionDetails(@RequestPart PaymentSuccessDTO paymentSuccessDTO, @RequestPart List<OrderItemDTO> orderItemDTO, @RequestHeader("Authorization") String token){
        service.saveOrder(paymentSuccessDTO, orderItemDTO,token);
    }

    @PostMapping("/payment/failure")
    public ResponseEntity<String> saveTransactionFailure(){
        return new ResponseEntity<>("payment failed",HttpStatus.BAD_GATEWAY);
    }
}
