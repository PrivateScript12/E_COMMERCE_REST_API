package com.ecommerce.controller;

import com.ecommerce.dto.AddToCartRequest;
import com.ecommerce.dto.CartItemDto;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.User;
import com.ecommerce.service.CartService;
import com.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
@Tag(name = "Cart", description = "Shopping cart management APIs")
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "Bearer Authentication")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private UserService userService;
    
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        return user.getId();
    }
    
    @GetMapping
    @Operation(summary = "Get cart contents", description = "Retrieve all items in the user's cart")
    public ResponseEntity<List<CartItemDto>> getCart() {
        try {
            Long userId = getCurrentUserId();
            List<CartItem> cartItems = cartService.getUserCart(userId);
            
            List<CartItemDto> cartItemDtos = cartItems.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(cartItemDtos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/add")
    @Operation(summary = "Add item to cart", description = "Add a product to the user's cart")
    public ResponseEntity<CartItemDto> addToCart(@Valid @RequestBody AddToCartRequest request) {
        try {
            Long userId = getCurrentUserId();
            CartItem cartItem = cartService.addToCart(userId, request.getProductId(), request.getQuantity());
            
            CartItemDto cartItemDto = convertToDto(cartItem);
            return ResponseEntity.ok(cartItemDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/item/{id}")
    @Operation(summary = "Update cart item quantity", description = "Update the quantity of a specific cart item")
    public ResponseEntity<CartItemDto> updateCartItemQuantity(
            @Parameter(description = "Cart item ID") @PathVariable Long id,
            @Parameter(description = "New quantity") @RequestParam Integer quantity) {
        try {
            Long userId = getCurrentUserId();
            CartItem cartItem = cartService.updateCartItemQuantity(userId, id, quantity);
            
            CartItemDto cartItemDto = convertToDto(cartItem);
            return ResponseEntity.ok(cartItemDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/item/{id}")
    @Operation(summary = "Remove item from cart", description = "Remove a specific item from the user's cart")
    public ResponseEntity<Void> removeFromCart(@Parameter(description = "Cart item ID") @PathVariable Long id) {
        try {
            Long userId = getCurrentUserId();
            cartService.removeFromCart(userId, id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/clear")
    @Operation(summary = "Clear cart", description = "Remove all items from the user's cart")
    public ResponseEntity<Void> clearCart() {
        try {
            Long userId = getCurrentUserId();
            cartService.clearCart(userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/total")
    @Operation(summary = "Get cart total", description = "Get the total price of all items in the cart")
    public ResponseEntity<BigDecimal> getCartTotal() {
        try {
            Long userId = getCurrentUserId();
            BigDecimal total = cartService.getCartTotal(userId);
            return ResponseEntity.ok(total);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/count")
    @Operation(summary = "Get cart item count", description = "Get the total number of items in the cart")
    public ResponseEntity<Integer> getCartItemCount() {
        try {
            Long userId = getCurrentUserId();
            int count = cartService.getCartItemCount(userId);
            return ResponseEntity.ok(count);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    private CartItemDto convertToDto(CartItem cartItem) {
        return new CartItemDto(
                cartItem.getId(),
                cartItem.getProduct().getId(),
                cartItem.getProduct().getName(),
                cartItem.getProduct().getDescription(),
                cartItem.getProduct().getPrice(),
                cartItem.getProduct().getImageUrl(),
                cartItem.getQuantity(),
                cartItem.getTotalPrice(),
                cartItem.getCreatedAt(),
                cartItem.getUpdatedAt()
        );
    }
} 