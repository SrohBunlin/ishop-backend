package com.inet.ishop.repository;

import com.inet.ishop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // បន្ថែមមុខងារស្វែងរកទំនិញតាមឈ្មោះ
    List<Product> findByNameContainingIgnoreCase(String name);
    @Query("SELECT p FROM Product p WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "CAST(p.price AS string) LIKE CONCAT('%', :query, '%') OR " +
            "CAST(p.id AS string) LIKE CONCAT('%', :query, '%')")
    List<Product> searchGlobal(@Param("query") String query);
    // ស្វែងរកទំនិញដែលមានក្នុងស្តុកតិចជាងចំនួនកំណត់ (សម្រាប់ Management System)
    List<Product> findByStockQuantityLessThan(Integer threshold);
}
