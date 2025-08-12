package com.ecommerce.service;

import com.ecommerce.entity.Product;
import com.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    
    @Mock
    private ProductRepository productRepository;
    
    @InjectMocks
    private ProductService productService;
    
    private Product testProduct;
    private Pageable pageable;
    
    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(new BigDecimal("99.99"));
        testProduct.setStockQuantity(10);
        testProduct.setCategory("Electronics");
        
        pageable = PageRequest.of(0, 10);
    }
    
    @Test
    void getAllProducts_ShouldReturnPageOfProducts() {
        // Arrange
        List<Product> products = Arrays.asList(testProduct);
        Page<Product> productPage = new PageImpl<>(products, pageable, 1);
        when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);
        
        // Act
        Page<Product> result = productService.getAllProducts(pageable);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testProduct, result.getContent().get(0));
        verify(productRepository).findAll(pageable);
    }
    
    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        
        // Act
        Optional<Product> result = productService.getProductById(1L);
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals(testProduct, result.get());
        verify(productRepository).findById(1L);
    }
    
    @Test
    void getProductById_WhenProductDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        
        // Act
        Optional<Product> result = productService.getProductById(1L);
        
        // Assert
        assertFalse(result.isPresent());
        verify(productRepository).findById(1L);
    }
    
    @Test
    void createProduct_ShouldReturnSavedProduct() {
        // Arrange
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        
        // Act
        Product result = productService.createProduct(testProduct);
        
        // Assert
        assertNotNull(result);
        assertEquals(testProduct, result);
        verify(productRepository).save(testProduct);
    }
    
    @Test
    void updateProduct_WhenProductExists_ShouldReturnUpdatedProduct() {
        // Arrange
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(new BigDecimal("149.99"));
        
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
        
        // Act
        Product result = productService.updateProduct(1L, updatedProduct);
        
        // Assert
        assertNotNull(result);
        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }
    
    @Test
    void updateProduct_WhenProductDoesNotExist_ShouldThrowException() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            productService.updateProduct(1L, testProduct);
        });
        verify(productRepository).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }
    
    @Test
    void deleteProduct_WhenProductExists_ShouldDeleteProduct() {
        // Arrange
        when(productRepository.existsById(1L)).thenReturn(true);
        
        // Act
        productService.deleteProduct(1L);
        
        // Assert
        verify(productRepository).existsById(1L);
        verify(productRepository).deleteById(1L);
    }
    
    @Test
    void deleteProduct_WhenProductDoesNotExist_ShouldThrowException() {
        // Arrange
        when(productRepository.existsById(1L)).thenReturn(false);
        
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            productService.deleteProduct(1L);
        });
        verify(productRepository).existsById(1L);
        verify(productRepository, never()).deleteById(any());
    }
} 