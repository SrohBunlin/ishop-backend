package com.inet.ishop.controller;

import com.inet.ishop.entity.Product;
import com.inet.ishop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "https://i-knet.com", allowedHeaders = "*")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // ១. មើលផលិតផលទាំងអស់ (Public - ប្រើតែមួយបានហើយ)
    @GetMapping("/all")
   // @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // ២. មើលផលិតផលតាម ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // ៣. បន្ថែមផលិតផលថ្មី (ADMIN តែប៉ុណ្ណោះ)
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return new ResponseEntity<>(productService.saveProduct(product), HttpStatus.CREATED);
    }

    // ៤. កែសម្រួលផលិតផល (ADMIN តែប៉ុណ្ណោះ)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        Product updatedProduct = productService.updateProduct(id, productDetails);
        return ResponseEntity.ok(updatedProduct);
    }

    // ៥. លុបផលិតផល (ADMIN តែប៉ុណ្ណោះ - បន្ថែម Security)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("ទំនិញដែលមាន ID " + id + " ត្រូវបានលុបចេញពីប្រព័ន្ធដោយជោគជ័យ!");
    }
}