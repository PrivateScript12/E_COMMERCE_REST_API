//transfer podataka za api operacije
// kontrolisanje proizvoda
// koristi bean validaciju

package com.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public class ProductDto {
    
    private Long id;
    
    @NotBlank(message = "Product name is required")
    @Size(min = 1, max = 255, message = "Product name must be between 1 and 255 characters")
    private String name;
    
    @Size(max = 500, message = "Short description cannot exceed 500 characters")
    private String shortDescription;
    
    @Size(max = 2000, message = "Full description cannot exceed 2000 characters")
    private String fullDescription;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    
    @NotNull(message = "Stock quantity is required")
    @Positive(message = "Stock quantity must be positive")
    private Integer stockQuantity;
    
    @Size(max = 255, message = "Category cannot exceed 255 characters")
    private String category;
    
    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    private String imageUrl;
    
    private List<String> images;
    
    private String technicalSpecifications;
    
    // Constructors
    public ProductDto() {}
    
    public ProductDto(Long id, String name, String shortDescription, String fullDescription, BigDecimal price, Integer stockQuantity, String category, String imageUrl, List<String> images, String technicalSpecifications) {
        this.id = id;
        this.name = name;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.imageUrl = imageUrl;
        this.images = images;
        this.technicalSpecifications = technicalSpecifications;
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