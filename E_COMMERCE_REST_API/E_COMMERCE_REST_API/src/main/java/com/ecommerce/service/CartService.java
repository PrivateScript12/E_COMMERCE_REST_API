// biznis logika za korpu
// dodavanje, brisanje, update korpe, total kalkulacije
// cart controller i dto
// spring data i patination


package com.ecommerce.service;

import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private UserService userService;
    
    public List<CartItem> getUserCart(Long userId) {
        Optional<User> user = userService.findById(userId);
        if (user.isPresent()) {
            return cartItemRepository.findByUser(user.get());
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }
    
    public CartItem addToCart(Long userId, Long productId, Integer quantity) {
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        
        Optional<Product> product = productService.getProductById(productId);
        if (product.isEmpty()) {
            throw new RuntimeException("Product not found with id: " + productId);
        }
        
        // Check if product is already in cart
        Optional<CartItem> existingCartItem = cartItemRepository.findByUserAndProductId(user.get(), productId);
        
        if (existingCartItem.isPresent()) {
            // Update quantity
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            return cartItemRepository.save(cartItem);
        } else {
            // Create new cart item
            CartItem cartItem = new CartItem(user.get(), product.get(), quantity);
            return cartItemRepository.save(cartItem);
        }
    }
    
    public CartItem updateCartItemQuantity(Long userId, Long cartItemId, Integer quantity) {
        Optional<CartItem> cartItem = cartItemRepository.findByUserIdAndCartItemId(userId, cartItemId);
        if (cartItem.isPresent()) {
            CartItem item = cartItem.get();
            item.setQuantity(quantity);
            return cartItemRepository.save(item);
        } else {
            throw new RuntimeException("Cart item not found or does not belong to user");
        }
    }
    
    public void removeFromCart(Long userId, Long cartItemId) {
        Optional<CartItem> cartItem = cartItemRepository.findByUserIdAndCartItemId(userId, cartItemId);
        if (cartItem.isPresent()) {
            cartItemRepository.delete(cartItem.get());
        } else {
            throw new RuntimeException("Cart item not found or does not belong to user");
        }
    }
    
    public void clearCart(Long userId) {
        Optional<User> user = userService.findById(userId);
        if (user.isPresent()) {
            cartItemRepository.deleteByUser(user.get());
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }
    
    public BigDecimal getCartTotal(Long userId) {
        List<CartItem> cartItems = getUserCart(userId);
        return cartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public int getCartItemCount(Long userId) {
        List<CartItem> cartItems = getUserCart(userId);
        return cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
} 