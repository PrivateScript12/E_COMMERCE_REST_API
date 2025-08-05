package com.ecommerce.controller;

import com.ecommerce.dto.ProductDto;
import com.ecommerce.entity.Product;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.ProductDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Product management APIs")
@CrossOrigin(origins = "*")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductDataService productDataService;
    
    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve all products with pagination, sorting, and filtering")
    public ResponseEntity<Page<Product>> getAllProducts(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "ASC") String sortDir,
            @Parameter(description = "Product name filter") @RequestParam(required = false) String name,
            @Parameter(description = "Category filter") @RequestParam(required = false) String category,
            @Parameter(description = "Minimum price filter") @RequestParam(required = false) Double minPrice,
            @Parameter(description = "Maximum price filter") @RequestParam(required = false) Double maxPrice) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Product> products;
        if (name != null || category != null || minPrice != null || maxPrice != null) {
            products = productService.getProductsByFilters(name, category, minPrice, maxPrice, pageable);
        } else {
            products = productService.getAllProducts(pageable);
        }
        
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID")
    public ResponseEntity<Product> getProductById(@Parameter(description = "Product ID") @PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/category/{category}")
    @Operation(summary = "Get products by category", description = "Retrieve products filtered by category")
    public ResponseEntity<Page<Product>> getProductsByCategory(
            @Parameter(description = "Category name") @PathVariable String category,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.getProductsByCategory(category, pageable);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search products by name", description = "Search products by name with pagination")
    public ResponseEntity<Page<Product>> searchProducts(
            @Parameter(description = "Search term") @RequestParam String name,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.searchProductsByName(name, pageable);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/categories")
    @Operation(summary = "Get all categories", description = "Retrieve all available product categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = productService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    
    @PostMapping
    @Operation(summary = "Create a new product", description = "Create a new product (Admin only)")
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setShortDescription(productDto.getShortDescription());
        product.setFullDescription(productDto.getFullDescription());
        product.setPrice(productDto.getPrice());
        product.setStockQuantity(productDto.getStockQuantity());
        product.setCategory(productDto.getCategory());
        product.setImageUrl(productDto.getImageUrl());
        product.setImages(productDto.getImages());
        product.setTechnicalSpecifications(productDto.getTechnicalSpecifications());
        
        Product savedProduct = productService.createProduct(product);
        return ResponseEntity.status(201).body(savedProduct);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update a product", description = "Update an existing product (Admin only)")
    public ResponseEntity<Product> updateProduct(
            @Parameter(description = "Product ID") @PathVariable Long id,
            @RequestBody ProductDto productDto) {
        try {
            Product product = new Product();
            product.setName(productDto.getName());
            product.setShortDescription(productDto.getShortDescription());
            product.setFullDescription(productDto.getFullDescription());
            product.setPrice(productDto.getPrice());
            product.setStockQuantity(productDto.getStockQuantity());
            product.setCategory(productDto.getCategory());
            product.setImageUrl(productDto.getImageUrl());
            product.setImages(productDto.getImages());
            product.setTechnicalSpecifications(productDto.getTechnicalSpecifications());
            
            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Delete a product (Admin only)")
    public ResponseEntity<Void> deleteProduct(@Parameter(description = "Product ID") @PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/reload")
    @Operation(summary = "Reload products from JSON", description = "Reload all products from products.json file (Admin only)")
    public ResponseEntity<String> reloadProductsFromJson() {
        try {
            if (!productDataService.isJsonFileAvailable()) {
                return ResponseEntity.badRequest().body("products.json file not found in resources directory");
            }
            
            productDataService.reloadProductsFromJson();
            long count = productDataService.getProductCount();
            return ResponseEntity.ok("Products reloaded successfully! Total products: " + count);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error reloading products: " + e.getMessage());
        }
    }
    
    @GetMapping("/count")
    @Operation(summary = "Get product count", description = "Get the total number of products in the database")
    public ResponseEntity<Long> getProductCount() {
        long count = productDataService.getProductCount();
        return ResponseEntity.ok(count);
    }
} 