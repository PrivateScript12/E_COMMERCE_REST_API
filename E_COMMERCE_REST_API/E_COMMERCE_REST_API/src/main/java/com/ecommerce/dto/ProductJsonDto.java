// dto za proizvode iz json fajla
// koristi json serijalizaciju
// za inicijalizaciju baze podataka proizvoda

package com.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ProductJsonDto {
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("price")
    private BigDecimal price;
    
    @JsonProperty("shortDescription")
    private String shortDescription;
    
    @JsonProperty("fullDescription")
    private String fullDescription;
    
    @JsonProperty("images")
    private List<String> images;
    
    @JsonProperty("technicalSpecifications")
    private Map<String, String> technicalSpecifications;
    
    @JsonProperty("stockQuantity")
    private Integer stockQuantity;
    
    @JsonProperty("category")
    private String category;
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
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
    
    public List<String> getImages() {
        return images;
    }
    
    public void setImages(List<String> images) {
        this.images = images;
    }
    
    public Map<String, String> getTechnicalSpecifications() {
        return technicalSpecifications;
    }
    
    public void setTechnicalSpecifications(Map<String, String> technicalSpecifications) {
        this.technicalSpecifications = technicalSpecifications;
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
} 