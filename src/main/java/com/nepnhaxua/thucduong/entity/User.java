package com.nepnhaxua.thucduong.entity;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
@CompoundIndexes({
    @CompoundIndex(name = "role_active_idx", def = "{'role': 1, 'isActive': 1}"),
    @CompoundIndex(name = "loyalty_tier_idx", def = "{'loyalty.tier': 1, 'loyalty.points': -1}")
})
public class User {
    @Id
    private String id;
    
    @Indexed(unique = true, sparse = true)
    private String email;
    
    @JsonIgnore
    private String password;
    
    private String fullName;
    
    @Indexed(unique = true, sparse = true)
    private String phoneNumber;
    
    private String role = "customer"; // customer, admin, author, support
    
    // Profile information
    private Profile profile;
    
    // Addresses
    private List<Address> addresses = new ArrayList<>();
    
    // Loyalty program
    private Loyalty loyalty;
    
    // User preferences
    private Preferences preferences;
    
    // Security settings
    @JsonIgnore
    private Security security;
    
    // Activity tracking
    private Activity activity;
    
    // GDPR consent
    private Consent consent;
    
    // Lists
    private List<String> wishlist = new ArrayList<>();
    private List<String> compareList = new ArrayList<>();
    private List<RecentlyViewed> recentlyViewed = new ArrayList<>();
    
    // Timestamps
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    
    // Status flags
    private boolean isActive = true;
    private boolean emailVerified = false;
    private boolean phoneVerified = false;
    
    @JsonIgnore
    private String resetPasswordToken;
    @JsonIgnore
    private LocalDateTime resetPasswordExpires;
    
    // Nested classes
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Profile {
        private String avatar;
        private LocalDateTime dateOfBirth;
        private String gender;
        private List<String> healthConditions = new ArrayList<>();
        private List<String> dietaryPreferences = new ArrayList<>();
        private List<String> allergies = new ArrayList<>();
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {
        private String id;
        private String type; // home, work, other
        private String recipientName;
        private String recipientPhone;
        private String street;
        private String ward;
        private String district;
        private String city;
        private String province;
        private String postalCode;
        private String landmark;
        private String deliveryInstructions;
        private Coordinates coordinates;
        private boolean isDefault;
        private boolean isVerified;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Coordinates {
        private Double lat;
        private Double lng;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Loyalty {
        private Integer points = 0;
        private String tier = "bronze"; // bronze, silver, gold, platinum
        private LocalDateTime tierExpiryDate;
        private Double lifetimeValue = 0.0;
        private String referralCode;
        private String referredBy;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Preferences {
        private boolean newsletter = true;
        private boolean smsNotifications = true;
        private boolean pushNotifications = true;
        private String language = "vi"; // vi, en
        private String currency = "VND";
        private String timezone = "Asia/Ho_Chi_Minh";
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Security {
        private boolean twoFactorEnabled = false;
        private String twoFactorSecret;
        private Integer loginAttempts = 0;
        private LocalDateTime lockedUntil;
        private List<String> passwordHistory = new ArrayList<>();
        private LocalDateTime lastPasswordChange;
        private List<SecurityQuestion> securityQuestions = new ArrayList<>();
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SecurityQuestion {
        private String question;
        private String answer; // hashed
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Activity {
        private LocalDateTime lastLogin;
        private LocalDateTime lastPurchase;
        private Integer totalOrders = 0;
        private Double totalSpent = 0.0;
        private Double averageOrderValue = 0.0;
        private LocalDateTime lastActivityAt;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Consent {
        private ConsentItem marketing;
        private ConsentItem dataProcessing;
        private ConsentItem cookies;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConsentItem {
        private boolean given;
        private LocalDateTime timestamp;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentlyViewed {
        private String productId;
        private LocalDateTime viewedAt;
    }
}
