package com.ecommerce.service;

import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.repository.CartItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    
    @Mock
    private CartItemRepository cartItemRepository;
    
    @Mock
    private ProductService productService;
    
    @Mock
    private UserService userService;
    
    @InjectMocks
    private CartService cartService;
    
    private User testUser;
    private Product testProduct;
    private CartItem testCartItem;
    
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setPrice(new BigDecimal("99.99"));
        
        testCartItem = new CartItem();
        testCartItem.setId(1L);
        testCartItem.setUser(testUser);
        testCartItem.setProduct(testProduct);
        testCartItem.setQuantity(2);
    }
    
    @Test
    void getUserCart_WhenUserExists_ShouldReturnCartItems() {
        // Arrange
        List<CartItem> cartItems = Arrays.asList(testCartItem);
        when(userService.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartItemRepository.findByUser(testUser)).thenReturn(cartItems);
        
        // Act
        List<CartItem> result = cartService.getUserCart(1L);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testCartItem, result.get(0));
        verify(userService).findById(1L);
        verify(cartItemRepository).findByUser(testUser);
    }
    
    @Test
    void getUserCart_WhenUserDoesNotExist_ShouldThrowException() {
        // Arrange
        when(userService.findById(1L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            cartService.getUserCart(1L);
        });
        verify(userService).findById(1L);
        verify(cartItemRepository, never()).findByUser(any());
    }
    
    @Test
    void addToCart_WhenProductNotInCart_ShouldCreateNewCartItem() {
        // Arrange
        when(userService.findById(1L)).thenReturn(Optional.of(testUser));
        when(productService.getProductById(1L)).thenReturn(Optional.of(testProduct));
        when(cartItemRepository.findByUserAndProductId(testUser, 1L)).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(testCartItem);
        
        // Act
        CartItem result = cartService.addToCart(1L, 1L, 2);
        
        // Assert
        assertNotNull(result);
        assertEquals(testCartItem, result);
        verify(userService).findById(1L);
        verify(productService).getProductById(1L);
        verify(cartItemRepository).findByUserAndProductId(testUser, 1L);
        verify(cartItemRepository).save(any(CartItem.class));
    }
    
    @Test
    void addToCart_WhenProductAlreadyInCart_ShouldUpdateQuantity() {
        // Arrange
        CartItem existingCartItem = new CartItem();
        existingCartItem.setQuantity(1);
        
        when(userService.findById(1L)).thenReturn(Optional.of(testUser));
        when(productService.getProductById(1L)).thenReturn(Optional.of(testProduct));
        when(cartItemRepository.findByUserAndProductId(testUser, 1L)).thenReturn(Optional.of(existingCartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(existingCartItem);
        
        // Act
        CartItem result = cartService.addToCart(1L, 1L, 2);
        
        // Assert
        assertNotNull(result);
        assertEquals(3, existingCartItem.getQuantity()); // 1 + 2
        verify(cartItemRepository).save(existingCartItem);
    }
    
    @Test
    void addToCart_WhenUserDoesNotExist_ShouldThrowException() {
        // Arrange
        when(userService.findById(1L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            cartService.addToCart(1L, 1L, 2);
        });
        verify(userService).findById(1L);
        verify(productService, never()).getProductById(any());
    }
    
    @Test
    void addToCart_WhenProductDoesNotExist_ShouldThrowException() {
        // Arrange
        when(userService.findById(1L)).thenReturn(Optional.of(testUser));
        when(productService.getProductById(1L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            cartService.addToCart(1L, 1L, 2);
        });
        verify(userService).findById(1L);
        verify(productService).getProductById(1L);
        verify(cartItemRepository, never()).findByUserAndProductId(any(), any());
    }
    
    @Test
    void updateCartItemQuantity_WhenCartItemExists_ShouldUpdateQuantity() {
        // Arrange
        when(cartItemRepository.findByUserIdAndCartItemId(1L, 1L)).thenReturn(Optional.of(testCartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(testCartItem);
        
        // Act
        CartItem result = cartService.updateCartItemQuantity(1L, 1L, 5);
        
        // Assert
        assertNotNull(result);
        assertEquals(5, testCartItem.getQuantity());
        verify(cartItemRepository).findByUserIdAndCartItemId(1L, 1L);
        verify(cartItemRepository).save(testCartItem);
    }
    
    @Test
    void updateCartItemQuantity_WhenCartItemDoesNotExist_ShouldThrowException() {
        // Arrange
        when(cartItemRepository.findByUserIdAndCartItemId(1L, 1L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            cartService.updateCartItemQuantity(1L, 1L, 5);
        });
        verify(cartItemRepository).findByUserIdAndCartItemId(1L, 1L);
        verify(cartItemRepository, never()).save(any());
    }
    
    @Test
    void removeFromCart_WhenCartItemExists_ShouldDeleteCartItem() {
        // Arrange
        when(cartItemRepository.findByUserIdAndCartItemId(1L, 1L)).thenReturn(Optional.of(testCartItem));
        
        // Act
        cartService.removeFromCart(1L, 1L);
        
        // Assert
        verify(cartItemRepository).findByUserIdAndCartItemId(1L, 1L);
        verify(cartItemRepository).delete(testCartItem);
    }
    
    @Test
    void removeFromCart_WhenCartItemDoesNotExist_ShouldThrowException() {
        // Arrange
        when(cartItemRepository.findByUserIdAndCartItemId(1L, 1L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            cartService.removeFromCart(1L, 1L);
        });
        verify(cartItemRepository).findByUserIdAndCartItemId(1L, 1L);
        verify(cartItemRepository, never()).delete(any());
    }
} 