// cart item data access layer
// user specitic operacije korpe
// spring data jpa


package com.ecommerce.service;

import com.ecommerce.dto.ProductJsonDto;
import com.ecommerce.entity.Product;
import com.ecommerce.repository.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class ProductDataService {
    
    @Autowired
    private ProductRepository productRepository;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Load products from JSON file and replace existing data
     */
    @Transactional
    public void reloadProductsFromJson() throws IOException {
        // Clear existing products
        productRepository.deleteAll();
        
        // Load from JSON file
        ClassPathResource resource = new ClassPathResource("products.json");
        InputStream inputStream = resource.getInputStream();
        
        List<ProductJsonDto> productDtos = objectMapper.readValue(inputStream, new TypeReference<List<ProductJsonDto>>() {});
        
        for (ProductJsonDto dto : productDtos) {
            Product product = new Product();
            product.setName(dto.getName());
            product.setShortDescription(dto.getShortDescription());
            product.setFullDescription(dto.getFullDescription());
            product.setPrice(dto.getPrice());
            product.setStockQuantity(dto.getStockQuantity());
            product.setCategory(dto.getCategory());
            product.setImages(dto.getImages());
            
            // Convert technical specifications to JSON string
            if (dto.getTechnicalSpecifications() != null) {
                String techSpecsJson = objectMapper.writeValueAsString(dto.getTechnicalSpecifications());
                product.setTechnicalSpecifications(techSpecsJson);
            }
            
            // Set primary image URL from images list
            if (dto.getImages() != null && !dto.getImages().isEmpty()) {
                product.setImageUrl(dto.getImages().get(0));
            }
            
            productRepository.save(product);
        }
    }
    
    /**
     * Get the count of products currently in the database
     */
    public long getProductCount() {
        return productRepository.count();
    }
    
    /**
     * Check if products.json file exists
     */
    public boolean isJsonFileAvailable() {
        try {
            ClassPathResource resource = new ClassPathResource("products.json");
            return resource.exists();
        } catch (Exception e) {
            return false;
        }
    }
} 