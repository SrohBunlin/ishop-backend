package com.inet.ishop.repository;

import com.inet.ishop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // គណនាចំណូលសរុបដែលជោគជ័យ (COMPLETED)
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = 'COMPLETED'")
    Double sumTotalRevenue();

    // រាប់ចំនួនការកុម្ម៉ង់ទាំងអស់
    @Query("SELECT COUNT(o) FROM Order o")
    Long countAllOrders();
}
