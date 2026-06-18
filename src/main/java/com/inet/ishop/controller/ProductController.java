package com.inet.ishop.controller;

import com.inet.ishop.entity.Product;
import com.inet.ishop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "https://i-knet.com", allowedHeaders = "*")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final String uploadDir = "/app/images/";

    // ១. មើលផលិតផលទាំងអស់ (Public)
    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // ២. មើលផលិតផលតាម ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // ៣. បន្ថែមផលិតផលថ្មី (ADMIN តែប៉ុណ្ណោះ) - កែសម្រួលឱ្យទទួលទាំងទិន្នន័យ និងរូបភាពក្នុងពេលតែមួយ
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProduct(
            @RequestParam("name") String name,
            @RequestParam("price") double price,
            @RequestParam("stockQuantity") int stockQuantity,
            @RequestParam("image") MultipartFile file) {

        try {
            // រៀបចំរក្សាទុកឯកសាររូបភាពចូលទៅក្នុង Folder /app/images/
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            // បង្កើតលីង URL រូបភាពដើម្បីញាត់ចូល Database
            String imageUrl = "/images/" + fileName;

            // បង្កើត Object ផលិតផលថ្មី រួចរក្សាទុកទៅក្នុង Database តាម Service
            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setStockQuantity(stockQuantity);
            product.setImageUrl(imageUrl);

            Product savedProduct = productService.saveProduct(product);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("មិនអាចរក្សាទុករូបភាពបានឡើយ: " + e.getMessage());
        }
    }

    // ៤. កែសម្រួលផលិតផល (ADMIN តែប៉ុណ្ណោះ) - បំពេញកូដឱ្យដើរពិតប្រាកដ
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("price") Double price,
            @RequestParam("stockQuantity") Integer stockQuantity,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            // ទាញយកទិន្នន័យផលិតផលចាស់ពី Database មកពិនិត្យ
            Product existingProduct = productService.getProductById(id);
            if (existingProduct == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("រកមិនឃើញទំនិញនេះឡើយ!");
            }

            // កែប្រែទិន្នន័យអក្សរ និងលេខ
            existingProduct.setName(name);
            existingProduct.setPrice(price);
            existingProduct.setStockQuantity(stockQuantity);

            // ប្រសិនបើអ្នកប្រើប្រាស់មានការ Upload រូបភាពថ្មីមកជំនួស
            if (image != null && !image.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                try (InputStream inputStream = image.getInputStream()) {
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                }
                // ប្តូរលីងរូបភាពថ្មី
                existingProduct.setImageUrl("/images/" + fileName);
            }

            // រក្សាទុកការផ្លាស់ប្តូរចូល Database
            Product updatedProduct = productService.saveProduct(existingProduct);
            return ResponseEntity.ok(updatedProduct);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("មានបញ្ហាក្នុងការរក្សាទុករូបភាពថ្មី: " + e.getMessage());
        }
    }

    // ៥. លុបផលិតផល (ADMIN តែប៉ុណ្ណោះ)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("ទំនិញដែលមាន ID " + id + " ត្រូវបានលុបចេញពីប្រព័ន្ធដោយជោគជ័យ!");
    }
}