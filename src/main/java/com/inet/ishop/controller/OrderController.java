package com.inet.ishop.controller;

import com.inet.ishop.entity.Order;
import com.inet.ishop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/add")
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        System.out.println("--- Request received for Order ---");
        try {
            Order savedOrder = orderService.saveOrder(order);
            return ResponseEntity.ok(savedOrder);
        } catch (Exception e) {
            System.out.println("Error saving order: " + e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate){
        String newStatus = statusUpdate.get("status");
        Order updateOrder = orderService.updateStatus(id, newStatus);
        return ResponseEntity.ok(updateOrder);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getOrderStats() {
        // ហៅតាមរយៈ Service វិញ (យើងនឹងទៅបង្កើត Method នេះក្នុង Service បន្ទាប់)
        return ResponseEntity.ok(orderService.getOrderStatistics());
    }
}
