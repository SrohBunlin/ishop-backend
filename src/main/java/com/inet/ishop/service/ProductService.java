package com.inet.ishop.service;

import com.inet.ishop.entity.Product;
import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product saveProduct(Product product);
    Product getProductById(Long id);
    Product updateProduct(Long id, Product productDetails);
    void deleteProduct(Long id);
    void updateStock(Long productId, Integer quantity);
}