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

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addProduct(
            @RequestParam("name") String name,
            @RequestParam("price") double price,
            @RequestPart("image") MultipartFile file) {

        try {
            // ១. កំណត់ទីតាំង Folder ផ្ទុករូបភាព (ត្រូវនឹងទីតាំងដែលយើងចង Volume ក្នុង Docker)
            String uploadDir = "/images/";
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename(); // បង្កើតឈ្មោះកុំឱ្យជាន់គ្នា
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath); // បង្កើត Folder បើមិនទាន់មាន
            }

            // ២. រក្សាទុកឯកសាររូបភាពពិតប្រាកដចូលទៅក្នុង Folder
            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            // ៣. បង្កើតលីង URL រូបភាពដើម្បីញាត់ចូល Database (ឧទាហរណ៍៖ /images/1712345_iphone.png)
            String imageUrl = "/images/" + fileName;

            // កូដរក្សាទុកទៅក្នុង Database របស់ប្អូន...
            // productRepository.save(new Product(name, price, imageUrl));

            return ResponseEntity.ok("Product added successfully with image path: " + imageUrl);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not save image: " + e.getMessage());
        }
    }
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