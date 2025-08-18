package com.nepnhaxua.thucduong.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.*;

import java.time.Duration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MongoIndexConfig {
    
    private final MongoTemplate mongoTemplate;
    
    @Bean
    @ConditionalOnProperty(
        name = "mongodb.indexes.create-on-startup",
        havingValue = "true"
    )
    public CommandLineRunner createIndexes() {
        return args -> {
            log.info("Starting MongoDB index creation...");
            
            try {
                createUserIndexes();
                createProductIndexes();
                createOrderIndexes();
                createCartIndexes();
                createReviewIndexes();
                createBlogIndexes();
                createAnalyticsIndexes();
                createChatIndexes();
                
                log.info("MongoDB indexes created successfully");
            } catch (Exception e) {
                log.error("Error creating MongoDB indexes", e);
            }
        };
    }
    
    private void createUserIndexes() {
        // Email unique index
        mongoTemplate.indexOps("users")
                .ensureIndex(new Index()
                        .on("email", Sort.Direction.ASC)
                        .unique()
                        .sparse());
        
        // Phone number unique index
        mongoTemplate.indexOps("users")
                .ensureIndex(new Index()
                        .on("phoneNumber", Sort.Direction.ASC)
                        .unique()
                        .sparse());
        
        // Role and active status compound index
        mongoTemplate.indexOps("users")
                .ensureIndex(new Index()
                        .on("role", Sort.Direction.ASC)
                        .on("isActive", Sort.Direction.ASC));
        
        // Loyalty tier index
        mongoTemplate.indexOps("users")
                .ensureIndex(new Index()
                        .on("loyalty.tier", Sort.Direction.ASC)
                        .on("loyalty.points", Sort.Direction.DESC));
        
        // Text search index
        mongoTemplate.indexOps("users")
                .ensureIndex(new TextIndexDefinition.TextIndexDefinitionBuilder()
                        .onField("fullName", 10F)
                        .onField("email", 5F)
                        .onField("phoneNumber", 5F)
                        .build());
        
        log.info("User indexes created");
    }
    
    private void createProductIndexes() {
        // Slug unique index
        mongoTemplate.indexOps("products")
                .ensureIndex(new Index()
                        .on("slug", Sort.Direction.ASC)
                        .unique());
        
        // SKU unique index
        mongoTemplate.indexOps("products")
                .ensureIndex(new Index()
                        .on("sku", Sort.Direction.ASC)
                        .unique()
                        .sparse());
        
        // Category compound index
        mongoTemplate.indexOps("products")
                .ensureIndex(new Index()
                        .on("category.main", Sort.Direction.ASC)
                        .on("flags.isActive", Sort.Direction.ASC)
                        .on("pricing.regular", Sort.Direction.ASC));
        
        // Featured products index
        mongoTemplate.indexOps("products")
                .ensureIndex(new Index()
                        .on("flags.isFeatured", Sort.Direction.ASC)
                        .on("flags.isActive", Sort.Direction.ASC)
                        .on("createdAt", Sort.Direction.DESC));
        
        // Rating index
        mongoTemplate.indexOps("products")
                .ensureIndex(new Index()
                        .on("ratings.average", Sort.Direction.DESC)
                        .on("ratings.count", Sort.Direction.DESC));
        
        // Health benefits index
        mongoTemplate.indexOps("products")
                .ensureIndex(new Index()
                        .on("healthBenefits.bodyPart", Sort.Direction.ASC)
                        .on("flags.isActive", Sort.Direction.ASC));
        
        // Text search index
        mongoTemplate.indexOps("products")
                .ensureIndex(new TextIndexDefinition.TextIndexDefinitionBuilder()
                        .onField("name", 10F)
                        .onField("searchKeywords", 8F)
                        .onField("tags", 5F)
                        .onField("description.short", 3F)
                        .onField("ingredients.name", 3F)
                        .onField("description.detailed", 1F)
                        .withDefaultLanguage("english")
                        .build());
        
        log.info("Product indexes created");
    }
    
    private void createOrderIndexes() {
        // Order number unique index
        mongoTemplate.indexOps("orders")
                .ensureIndex(new Index()
                        .on("orderNumber", Sort.Direction.ASC)
                        .unique());
        
        // User orders index
        mongoTemplate.indexOps("orders")
                .ensureIndex(new Index()
                        .on("userId", Sort.Direction.ASC)
                        .on("createdAt", Sort.Direction.DESC));
        
        // Status index
        mongoTemplate.indexOps("orders")
                .ensureIndex(new Index()
                        .on("status", Sort.Direction.ASC)
                        .on("createdAt", Sort.Direction.DESC));
        
        // Admin dashboard index
        mongoTemplate.indexOps("orders")
                .ensureIndex(new Index()
                        .on("status", Sort.Direction.ASC)
                        .on("payment.status", Sort.Direction.ASC)
                        .on("createdAt", Sort.Direction.DESC));
        
        log.info("Order indexes created");
    }
    
    private void createCartIndexes() {
        // User cart index
        mongoTemplate.indexOps("cart")
                .ensureIndex(new Index()
                        .on("userId", Sort.Direction.ASC)
                        .unique()
                        .sparse());
        
        // Session cart index
        mongoTemplate.indexOps("cart")
                .ensureIndex(new Index()
                        .on("sessionId", Sort.Direction.ASC)
                        .unique()
                        .sparse());
        
        // TTL index for cart expiration
        mongoTemplate.indexOps("cart")
                .ensureIndex(new Index()
                        .on("expiresAt", Sort.Direction.ASC)
                        .expire(Duration.ZERO));
        
        log.info("Cart indexes created");
    }
    
    private void createReviewIndexes() {
        // Product reviews index
        mongoTemplate.indexOps("reviews")
                .ensureIndex(new Index()
                        .on("productId", Sort.Direction.ASC)
                        .on("status", Sort.Direction.ASC)
                        .on("createdAt", Sort.Direction.DESC));
        
        // User reviews index
        mongoTemplate.indexOps("reviews")
                .ensureIndex(new Index()
                        .on("userId", Sort.Direction.ASC)
                        .on("createdAt", Sort.Direction.DESC));
        
        // Order review index
        mongoTemplate.indexOps("reviews")
                .ensureIndex(new Index()
                        .on("orderId", Sort.Direction.ASC));
        
        log.info("Review indexes created");
    }
    
    private void createBlogIndexes() {
        // Slug unique index
        mongoTemplate.indexOps("blogPosts")
                .ensureIndex(new Index()
                        .on("slug", Sort.Direction.ASC)
                        .unique());
        
        // Published posts index
        mongoTemplate.indexOps("blogPosts")
                .ensureIndex(new Index()
                        .on("status", Sort.Direction.ASC)
                        .on("publishedAt", Sort.Direction.DESC));
        
        // Category index
        mongoTemplate.indexOps("blogPosts")
                .ensureIndex(new Index()
                        .on("category", Sort.Direction.ASC)
                        .on("status", Sort.Direction.ASC)
                        .on("publishedAt", Sort.Direction.DESC));
        
        // Text search index
        mongoTemplate.indexOps("blogPosts")
                .ensureIndex(new TextIndexDefinition.TextIndexDefinitionBuilder()
                        .onField("title", 10F)
                        .onField("tags", 5F)
                        .onField("excerpt", 3F)
                        .onField("content", 1F)
                        .build());
        
        log.info("Blog indexes created");
    }
    
    private void createAnalyticsIndexes() {
        // Analytics query index
        mongoTemplate.indexOps("analytics")
                .ensureIndex(new Index()
                        .on("type", Sort.Direction.ASC)
                        .on("timestamp", Sort.Direction.DESC));
        
        // User analytics index
        mongoTemplate.indexOps("analytics")
                .ensureIndex(new Index()
                        .on("userId", Sort.Direction.ASC)
                        .on("type", Sort.Direction.ASC)
                        .on("timestamp", Sort.Direction.DESC));
        
        // TTL index for analytics data retention (90 days)
        mongoTemplate.indexOps("analytics")
                .ensureIndex(new Index()
                        .on("timestamp", Sort.Direction.DESC)
                        .expire(Duration.ofDays(90)));
        
        log.info("Analytics indexes created");
    }
    
    private void createChatIndexes() {
        // Session index
        mongoTemplate.indexOps("chatSessions")
                .ensureIndex(new Index()
                        .on("sessionId", Sort.Direction.ASC)
                        .on("createdAt", Sort.Direction.DESC));
        
        // User chat index
        mongoTemplate.indexOps("chatSessions")
                .ensureIndex(new Index()
                        .on("userId", Sort.Direction.ASC)
                        .on("lastMessageAt", Sort.Direction.DESC));
        
        // TTL index for chat sessions (30 days)
        mongoTemplate.indexOps("chatSessions")
                .ensureIndex(new Index()
                        .on("expiresAt", Sort.Direction.ASC)
                        .expire(Duration.ZERO));
        
        log.info("Chat indexes created");
    }
}
