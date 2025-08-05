package com.ecommerce.config;

import com.ecommerce.dto.ProductJsonDto;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Initialize sample products
        if (productRepository.count() == 0) {
            initializeProducts();
        }
        
        // Initialize sample users
        if (userRepository.count() == 0) {
            initializeUsers();
        }
    }
    
    private void initializeProducts() {
        try {
            // Load products from JSON file
            ObjectMapper mapper = new ObjectMapper();
            ClassPathResource resource = new ClassPathResource("products.json");
            InputStream inputStream = resource.getInputStream();
            
            List<ProductJsonDto> productDtos = mapper.readValue(inputStream, new TypeReference<List<ProductJsonDto>>() {});
            
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
                    String techSpecsJson = mapper.writeValueAsString(dto.getTechnicalSpecifications());
                    product.setTechnicalSpecifications(techSpecsJson);
                }
                
                // Set primary image URL from images list
                if (dto.getImages() != null && !dto.getImages().isEmpty()) {
                    product.setImageUrl(dto.getImages().get(0));
                }
                
                productRepository.save(product);
            }
            
            System.out.println("Products loaded from JSON file successfully!");
            System.out.println("Total products loaded: " + productDtos.size());
            
        } catch (IOException e) {
            System.err.println("Error loading products from JSON file: " + e.getMessage());
            // Fallback to hardcoded products if JSON loading fails
            initializeHardcodedProducts();
        }
    }
    
    private void initializeHardcodedProducts() {
        Product[] products = {
            new Product("+GSC 2,8 'SHEAR", 
                       "Compact and powerful cordless drill with 12V battery.", 
                       new BigDecimal("129.99"), 50, "Power Tools"),
            
            new Product("Professional Impact Driver GDR 18V-160", 
                       "High torque impact driver for heavy-duty applications.", 
                       new BigDecimal("199.99"), 30, "Power Tools"),
            
            new Product("Professional Angle Grinder GWS 7-115", 
                       "Lightweight angle grinder for versatile applications.", 
                       new BigDecimal("89.99"), 75, "Power Tools"),
            
            new Product("Professional Hammer Drill GBH 2-28 F", 
                       "Powerful hammer drill with vibration control.", 
                       new BigDecimal("349.99"), 25, "Power Tools"),
            
            new Product("Professional Jigsaw GST 150 BCE", 
                       "Ergonomic jigsaw with variable speed control.", 
                       new BigDecimal("159.99"), 40, "Power Tools"),
            
            new Product("Professional Rotary Hammer GBH 18V-26 F", 
                       "Cordless rotary hammer with SDS-plus chuck.", 
                       new BigDecimal("399.99"), 20, "Power Tools"),
            
            new Product("Professional Multi-Tool GOP 18V-28", 
                       "Versatile multi-tool for cutting, sanding, scraping.", 
                       new BigDecimal("229.99"), 35, "Power Tools"),
            
            new Product("Professional Circular Saw GKS 190", 
                       "Robust circular saw with precise cutting depth adjustment.", 
                       new BigDecimal("189.99"), 45, "Power Tools"),
            
            new Product("Professional Cordless Screwdriver GSR 18V-50", 
                       "Powerful and ergonomic cordless screwdriver.", 
                       new BigDecimal("159.99"), 60, "Power Tools"),
            
            new Product("Professional Planer GHO 26-82", 
                       "Efficient planer with smooth finish and easy handling.", 
                       new BigDecimal("219.99"), 30, "Power Tools"),
            
            new Product("Impact Drill GSB20-2RE keyed plastic cas", 
                       "Sturdy metal gear housing for long lifetime.", 
                       new BigDecimal("279.99"), 25, "Power Tools"),
            
            new Product("Laser Reciver LR7", 
                       "Precise laser measure with Bluetooth connectivity.", 
                       new BigDecimal("149.99"), 40, "Measurement Tools"),
            
            new Product("Professional Cordless Rotary Hammer GBH 18V-26", 
                       "Cordless rotary hammer with high impact energy.", 
                       new BigDecimal("389.99"), 20, "Power Tools"),
            
            new Product("Professional Heat Gun GHG 20-63", 
                       "Variable temperature heat gun with ergonomic design.", 
                       new BigDecimal("119.99"), 50, "Power Tools"),
            
            new Product("GLL 12-22 G LI: Laser Level Green", 
                       "Durable, green beam line laser for reliable levelling.", 
                       new BigDecimal("179.99"), 35, "Measurement Tools"),
            
            new Product("Professional Cordless Reciprocating Saw GSA 18 V-LI", 
                       "Powerful cordless reciprocating saw for demolition work.", 
                       new BigDecimal("229.99"), 30, "Power Tools"),
            
            new Product("MARBLE CUTTING SAW", 
                       "The handy problem solver for hard materials.", 
                       new BigDecimal("99.99"), 45, "Power Tools"),
            
            new Product("Dust Extractor GDE 230 FC-S", 
                       "Virtually dust-free cutting of stone with large angle grinders", 
                       new BigDecimal("179.99"), 25, "Accessories"),
            
            new Product("ROTARY HAMMER GBH 12-52 DV", 
                       "The most powerful rotary hammer – with Vibration Control", 
                       new BigDecimal("239.99"), 30, "Power Tools"),
            
            new Product("DEMOLITION HAMMER GSH 5", 
                       "Longer lifetime with power that impresses in its class.", 
                       new BigDecimal("179.99"), 20, "Power Tools")
        };
        
        // Set image URLs for each product
        products[0].setImageUrl("images/product_1/product_1.jpg");
        products[1].setImageUrl("images/product_2/product_2.jpg");
        products[2].setImageUrl("images/product_3/product_3.jpg");
        products[3].setImageUrl("images/product_4/product_4.jpg");
        products[4].setImageUrl("images/product_5/product_5.jpg");
        products[5].setImageUrl("images/product_6/product_6.jpg");
        products[6].setImageUrl("images/product_7/product_7.jpg");
        products[7].setImageUrl("images/product_8/product_8.jpg");
        products[8].setImageUrl("images/product_9/product_9.jpg");
        products[9].setImageUrl("images/product_10/product_10.jpg");
        products[10].setImageUrl("images/product_11/product_11.jpg");
        products[11].setImageUrl("images/product_12/product_12.jpg");
        products[12].setImageUrl("images/product_13/product_13.jpg");
        products[13].setImageUrl("images/product_14/product_14.jpg");
        products[14].setImageUrl("images/product_15/product_15.jpg");
        products[15].setImageUrl("images/product_16/product_16.jpg");
        products[16].setImageUrl("images/product_17/product_17.jpg");
        products[17].setImageUrl("images/product_18/product_18.jpg");
        products[18].setImageUrl("images/product_19/product_19.jpg");
        products[19].setImageUrl("images/product_20/product_20.jpg");
        
        productRepository.saveAll(Arrays.asList(products));
        System.out.println("Professional power tools initialized successfully!");
        System.out.println("Total products loaded: " + products.length);
    }
    
    private void initializeUsers() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(User.Role.ADMIN);
        
        User user = new User();
        user.setUsername("user");
        user.setEmail("user@example.com");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setRole(User.Role.USER);
        
        userRepository.saveAll(Arrays.asList(admin, user));
        System.out.println("Sample users initialized successfully!");
        System.out.println("Admin credentials: admin / admin123");
        System.out.println("User credentials: user / user123");
    }
} 