package com.nepnhaxua.thucduong.repository;

import com.nepnhaxua.thucduong.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    // Basic queries
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByEmailAndIsActiveTrue(String email);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    
    // Find by reset token
    Optional<User> findByResetPasswordToken(String token);
    Optional<User> findByResetPasswordTokenAndResetPasswordExpiresAfter(String token, LocalDateTime date);
    
    // Role-based queries
    List<User> findByRole(String role);
    Page<User> findByRoleAndIsActive(String role, boolean isActive, Pageable pageable);
    
    // Loyalty queries
    List<User> findByLoyaltyTier(String tier);
    
    @Query("{ 'loyalty.points': { $gte: ?0 } }")
    List<User> findUsersWithMinimumPoints(int points);
    
    // Activity queries
    @Query("{ 'activity.lastLogin': { $lt: ?0 } }")
    List<User> findInactiveUsers(LocalDateTime before);
    
    @Query("{ 'createdAt': { $gte: ?0, $lte: ?1 } }")
    List<User> findNewUsersBetweenDates(LocalDateTime start, LocalDateTime end);
    
    // Full text search
    @Query("{ $text: { $search: ?0 } }")
    Page<User> searchUsers(String searchTerm, Pageable pageable);
    
    // Update operations
    @Query("{ '_id': ?0 }")
    @Update("{ $set: { 'activity.lastLogin': ?1, 'activity.lastActivityAt': ?1 } }")
    void updateLastLogin(String userId, LocalDateTime loginTime);
    
    @Query("{ '_id': ?0 }")
    @Update("{ $inc: { 'loyalty.points': ?1 } }")
    void updateLoyaltyPoints(String userId, int points);
    
    @Query("{ '_id': ?0 }")
    @Update("{ $push: { 'wishlist': ?1 } }")
    void addToWishlist(String userId, String productId);
    
    @Query("{ '_id': ?0 }")
    @Update("{ $pull: { 'wishlist': ?1 } }")
    void removeFromWishlist(String userId, String productId);
    
    // Complex aggregation queries (can be implemented with @Aggregation)
    @Query("{ 'addresses.city': ?0, 'isActive': true }")
    List<User> findActiveUsersByCity(String city);
    
    // Soft delete
    @Query("{ '_id': ?0 }")
    @Update("{ $set: { 'isActive': false, 'deletedAt': ?1 } }")
    void softDelete(String userId, LocalDateTime deletedAt);
    
    // Cleanup expired tokens
    @Query("{ 'resetPasswordExpires': { $lt: ?0 } }")
    @Update("{ $unset: { 'resetPasswordToken': '', 'resetPasswordExpires': '' } }")
    void cleanupExpiredTokens(LocalDateTime now);
}
