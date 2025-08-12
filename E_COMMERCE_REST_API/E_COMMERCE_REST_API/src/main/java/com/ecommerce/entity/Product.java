package com.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Product name is required")
    @Size(min = 1, max = 255, message = "Product name must be between 1 and 255 characters")
    @Column(nullable = false)
    private String name;
    
    @Size(max = 500, message = "Short description cannot exceed 500 characters")
    @Column(name = "short_description", length = 500)
    private String shortDescription;
    
    @Size(max = 2000, message = "Full description cannot exceed 2000 characters")
    @Column(name = "full_description", length = 2000)
    private String fullDescription;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @NotNull(message = "Stock quantity is required")
    @Positive(message = "Stock quantity must be positive")
    @Column(nullable = false)
    private Integer stockQuantity;
    
    @Size(max = 255, message = "Category cannot exceed 255 characters")
    private String category;
    
    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    private String imageUrl;
    
    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> images;
    
    @Column(name = "technical_specifications", columnDefinition = "TEXT")
    private String technicalSpecifications;
    
    // Constructors
    public Product() {}
    
    public Product(String name, String shortDescription, BigDecimal price, Integer stockQuantity, String category) {
        this.name = name;
        this.shortDescription = shortDescription;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
    }
    
    public Product(String name, String shortDescription, String fullDescription, BigDecimal price, Integer stockQuantity, String category) {
        this.name = name;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getShortDescription() {
        return shortDescription;
    }
    
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
    
    public String getFullDescription() {
        return fullDescription;
    }
    
    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }
    
    public String getDescription() {
        return shortDescription != null ? shortDescription : fullDescription;
    }
    
    public void setDescription(String description) {
        this.shortDescription = description;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public Integer getStockQuantity() {
        return stockQuantity;
    }
    
    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public List<String> getImages() {
        return images;
    }
    
    public void setImages(List<String> images) {
        this.images = images;
    }
    
    public String getTechnicalSpecifications() {
        return technicalSpecifications;
    }
    
    public void setTechnicalSpecifications(String technicalSpecifications) {
        this.technicalSpecifications = technicalSpecifications;
    }
} 