// pristupanje proizvodima u korpi
// user-specific cart management
//spring data jpa i query methode


package com.ecommerce.repository;

import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    
    List<CartItem> findByUser(User user);
    
    Optional<CartItem> findByUserAndProductId(User user, Long productId);
    
    @Query("SELECT ci FROM CartItem ci WHERE ci.user.id = :userId")
    List<CartItem> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT ci FROM CartItem ci WHERE ci.user.id = :userId AND ci.id = :cartItemId")
    Optional<CartItem> findByUserIdAndCartItemId(@Param("userId") Long userId, @Param("cartItemId") Long cartItemId);
    
    void deleteByUserAndProductId(User user, Long productId);
    
    void deleteByUser(User user);
} 