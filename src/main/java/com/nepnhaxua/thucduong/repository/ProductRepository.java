package com.nepnhaxua.thucduong.repository;

import com.nepnhaxua.thucduong.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    // Basic queries
    Optional<Product> findBySlug(String slug);

    Optional<Product> findBySku(String sku);

    Optional<Product> findByIdAndFlagsIsActiveTrue(String id);

    // Category queries
    Page<Product> findByCategoryMainAndFlagsIsActiveTrue(String category, Pageable pageable);

    Page<Product> findByCategoryMainAndCategorySubAndFlagsIsActiveTrue(
            String mainCategory, String subCategory, Pageable pageable);

    // Featured products
    List<Product> findByFlagsIsFeaturedTrueAndFlagsIsActiveTrueOrderByCreatedAtDesc();

    @Query("{ 'flags.isFeatured': true, 'flags.isActive': true }")
    Page<Product> findFeaturedProducts(Pageable pageable);

    // Price range queries
    @Query("{ 'pricing.regular': { $gte: ?0, $lte: ?1 }, 'flags.isActive': true }")
    Page<Product> findByPriceRange(Double minPrice, Double maxPrice, Pageable pageable);

    // Sale products
    @Query("{ 'pricing.sale': { $exists: true, $gt: 0 }, 'pricing.sale': { $lt: '$pricing.regular' }, 'flags.isActive': true }")
    Page<Product> findSaleProducts(Pageable pageable);

    // Tag queries
    @Query("{ 'tags': { $in: ?0 }, 'flags.isActive': true }")
    Page<Product> findByTags(List<String> tags, Pageable pageable);

    // Health benefit queries
    @Query("{ 'healthBenefits.bodyPart': ?0, 'flags.isActive': true }")
    List<Product> findByBodyPart(String bodyPart);

    // Stock queries
    @Query("{ 'inventory.quantity': { $lte: '$inventory.lowStockThreshold' }, 'flags.isActive': true }")
    List<Product> findLowStockProducts();

    @Query("{ 'inventory.quantity': 0, 'flags.isActive': true }")
    List<Product> findOutOfStockProducts();

    // Full text search
    @Query("{ $text: { $search: ?0 }, 'flags.isActive': true }")
    Page<Product> searchProducts(String searchTerm, Pageable pageable);

    // Complex search with score
    @Aggregation(pipeline = {
            "{ $match: { $text: { $search: ?0 }, 'flags.isActive': true } }",
            "{ $addFields: { score: { $meta: 'textScore' } } }",
            "{ $sort: { score: -1 } }"
    })
    Slice<Product> searchProductsWithScore(String searchTerm, Pageable pageable);

    // Update operations
    @Query("{ '_id': ?0 }")
    @Update("{ $inc: { 'inventory.quantity': ?1, 'inventory.reserved': ?2 } }")
    void updateInventory(String productId, int quantityChange, int reservedChange);

    @Query("{ '_id': ?0 }")
    @Update("{ $inc: { 'analytics.views': 1 } }")
    void incrementViews(String productId);

    @Query("{ '_id': ?0 }")
    @Update("{ $inc: { 'analytics.addedToCart': 1 } }")
    void incrementAddedToCart(String productId);

    @Query("{ '_id': ?0 }")
    @Update("{ $set: { 'ratings.average': ?1, 'ratings.count': ?2 } }")
    void updateRatings(String productId, Double average, Integer count);

    // Bestsellers (based on purchase count)
    @Aggregation(pipeline = {
            "{ $match: { 'flags.isActive': true } }",
            "{ $sort: { 'analytics.purchased': -1 } }",
            "{ $limit: ?0 }"
    })
    List<Product> findBestsellers(int limit);

    // New arrivals
    @Query("{ 'flags.isNew': true, 'flags.isActive': true }")
    Page<Product> findNewArrivals(Pageable pageable);

    // Products by manufacturer
    @Query("{ 'manufacturer.name': ?0, 'flags.isActive': true }")
    Page<Product> findByManufacturer(String manufacturerName, Pageable pageable);

    // Related products
    @Query("{ '_id': { $in: ?0 }, 'flags.isActive': true }")
    List<Product> findRelatedProducts(List<String> productIds);

    // Bulk operations
    @Query("{ '_id': { $in: ?0 } }")
    @Update("{ $set: { 'flags.isActive': ?1 } }")
    void updateActiveStatusBulk(List<String> productIds, boolean isActive);
}
