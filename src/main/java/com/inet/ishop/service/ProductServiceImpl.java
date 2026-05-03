package com.inet.ishop.service;



import com.inet.ishop.entity.Product;
import com.inet.ishop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor // ជួយបង្កើត Constructor សម្រាប់ inject repository
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void updateStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("រកមិនឃើញទំនិញឡើយ"));
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);
    }
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("រកមិនឃើញទំនិញដែលមាន ID: " + id));
    }

    @Override
    public Product updateProduct(Long id, Product productDetails) {
        // ១. ស្វែងរកទំនិញតាម ID បើមិនឃើញទេនឹងបោះ Error
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("រកមិនឃើញទំនិញដែលមាន ID: " + id));
        // ២. កែប្រែព័ត៌មានថ្មី
        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        product.setStockQuantity(productDetails.getStockQuantity());
        product.setImageUrl(productDetails.getImageUrl());

        // ក្នុងករណីមាន Category ដែរ
        if(productDetails.getCategory() != null) {
            product.setCategory(productDetails.getCategory());
        }

        // ៣. រក្សាទុកទិន្នន័យដែលបានកែប្រែចូលក្នុង Database វិញ
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("រកមិនឃើញទំនិញសម្រាប់លុបឡើយ ID: " + id));
        productRepository.delete(product);
    }

}