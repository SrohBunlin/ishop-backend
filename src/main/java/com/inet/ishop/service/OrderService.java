package com.inet.ishop.service;

import com.inet.ishop.entity.Order;
import com.inet.ishop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    public Map<String, Object> getOrderStatistics() {
        Map<String, Object> stats = new HashMap<>();

        // ហៅ Query ដែលប្អូនបានសរសេរក្នុង Repository
        Double totalRevenue = orderRepository.sumTotalRevenue();
        Long totalOrders = orderRepository.countAllOrders();

        // បញ្ចូលទិន្នន័យទៅក្នុង Map ដើម្បីផ្ញើទៅឱ្យ React
        stats.put("totalRevenue", totalRevenue != null ? totalRevenue : 0.0);
        stats.put("totalOrders", totalOrders != null ? totalOrders : 0);

        return stats;
    }
    @Transactional
    public Order saveOrder(Order order) {
        if (order.getItems() != null) {
            order.getItems().forEach(item -> item.setOrder(order));
        }
        return orderRepository.save(order);
    }
    public List<Order> getAllOrders() {
        return orderRepository.findAll(); // ទាញយកគ្រប់ Order ទាំងអស់រួមទាំង Items ផងដែរ
    }

    public Order updateStatus(Long id,String status){
        Order order = orderRepository.findById(id).orElseThrow(()-> new RuntimeException("រកមិនឃើញវិក្កយបត្រនេះឡើយ!"));

        order.setStatus(status);
        return orderRepository.save(order);
    }


}
