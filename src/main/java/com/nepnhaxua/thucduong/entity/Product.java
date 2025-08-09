package com.nepnhaxua.thucduong.entity;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
@CompoundIndexes({
    @CompoundIndex(name = "category_active_price_idx", def = "{'category.main': 1, 'flags.isActive': 1, 'pricing.regular': 1}"),
    @CompoundIndex(name = "tags_active_idx", def = "{'tags': 1, 'flags.isActive': 1}"),
    @CompoundIndex(name = "rating_idx", def = "{'ratings.average': -1, 'ratings.count': -1}"),
    @CompoundIndex(name = "featured_idx", def = "{'flags.isFeatured': 1, 'flags.isActive': 1, 'createdAt': -1}")
})
public class Product {
    @Id
    private String id;
    
    @TextIndexed(weight = 10)
    private String name;
    
    @Indexed(unique = true)
    private String slug;
    
    @Indexed(unique = true, sparse = true)
    private String sku;
    
    private Category category;
    private Pricing pricing;
    private Media media;
    
    @TextIndexed(weight = 3)
    private Description description;
    
    private List<Ingredient> ingredients = new ArrayList<>();
    private NutritionalInfo nutritionalInfo;
    private List<HealthBenefit> healthBenefits = new ArrayList<>();
    private List<Certification> certifications = new ArrayList<>();
    private Inventory inventory;
    private Manufacturer manufacturer;
    private Shipping shipping;
    private Ratings ratings;
    private RelatedProducts related;
    
    @TextIndexed(weight = 8)
    private List<String> searchKeywords = new ArrayList<>();
    private List<String> synonyms = new ArrayList<>();
    
    @TextIndexed(weight = 5)
    private List<String> tags = new ArrayList<>();
    private List<String> badges = new ArrayList<>();
    
    private Flags flags;
    private Analytics analytics;
    
    // Dates
    private LocalDateTime publishedAt;
    private LocalDateTime availableFrom;
    private LocalDateTime availableUntil;
    @Indexed
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    
    private Map<String, SEO> seo = new HashMap<>(); // key: language code (vi, en)
    
    // Nested classes
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category {
        @Indexed
        private String main;
        private String sub;
        private List<String> tags = new ArrayList<>();
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pricing {
        private Double cost;
        @Indexed
        private Double regular;
        @Indexed(sparse = true)
        private Double sale;
        private List<BulkPricing> bulkPricing = new ArrayList<>();
        private String currency = "VND";
        private boolean taxable = true;
        private String taxClass;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BulkPricing {
        private Integer minQuantity;
        private Double price;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Media {
        private List<Image> images = new ArrayList<>();
        private List<Video> videos = new ArrayList<>();
        private List<Document> documents = new ArrayList<>();
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Image {
        private String url;
        private String alt;
        private String caption;
        private boolean isPrimary;
        private Integer order;
        private Dimensions dimensions;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Video {
        private String url;
        private String thumbnail;
        private Integer duration;
        private String type; // product_demo, recipe, testimonial
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Document {
        private String name;
        private String url;
        private String type; // certificate, lab_report, user_manual
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Dimensions {
        private Integer width;
        private Integer height;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Description {
        private String shortDesc;
        private String detailed;
        private String usage;
        private String storage;
        private String warnings;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Ingredient {
        @TextIndexed(weight = 3)
        private String name;
        private Double percentage;
        private List<String> benefits = new ArrayList<>();
        private boolean isOrganic;
        private boolean allergen;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NutritionalInfo {
        private String servingSize;
        private Integer servingsPerPackage;
        private Integer calories;
        private Integer caloriesFromFat;
        private Map<String, Nutrient> nutrients = new HashMap<>();
        private List<Vitamin> vitamins = new ArrayList<>();
        private List<Mineral> minerals = new ArrayList<>();
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Nutrient {
        private Double amount;
        private String unit;
        private Double dailyValue;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Vitamin {
        private String name;
        private Double amount;
        private String unit;
        private Double dailyValue;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Mineral {
        private String name;
        private Double amount;
        private String unit;
        private Double dailyValue;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HealthBenefit {
        @Indexed
        private String bodyPart;
        private String benefit;
        private String description;
        private String scientificEvidence;
        private List<Study> studies = new ArrayList<>();
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Study {
        private String title;
        private String url;
        private Integer year;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Certification {
        private String name;
        private String certifier;
        private String certificateNumber;
        private LocalDateTime validUntil;
        private String documentUrl;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Inventory {
        @Indexed
        private Integer quantity;
        private Integer reserved;
        private String unit;
        private Integer lowStockThreshold;
        private Integer outOfStockThreshold;
        private Integer maxOrderQuantity;
        private Integer minOrderQuantity = 1;
        private boolean trackInventory = true;
        private boolean allowBackorder = false;
        private List<Location> locations = new ArrayList<>();
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location {
        private String warehouse;
        private Integer quantity;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Manufacturer {
        private String name;
        private String code;
        private Origin origin;
        private List<String> certifications = new ArrayList<>();
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Origin {
        private String country;
        private String region;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Shipping {
        private Double weight;
        private String weightUnit;
        private ShippingDimensions dimensions;
        private String shippingClass;
        private boolean freeShipping;
        private Double additionalShippingCost;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShippingDimensions {
        private Double length;
        private Double width;
        private Double height;
        private String unit;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Ratings {
        private Double average = 0.0;
        private Integer count = 0;
        private Map<Integer, Integer> distribution = new HashMap<>();
        private RatingAspects aspects;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RatingAspects {
        private Double quality;
        private Double value;
        private Double effectiveness;
        private Double taste;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RelatedProducts {
        private List<String> crossSells = new ArrayList<>();
        private List<String> upSells = new ArrayList<>();
        private List<String> similar = new ArrayList<>();
        private List<Bundle> frequentlyBoughtTogether = new ArrayList<>();
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Bundle {
        private List<String> products = new ArrayList<>();
        private Double discount;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Flags {
        @Indexed
        private boolean isActive = true;
        @Indexed
        private boolean isFeatured = false;
        private boolean isNew = false;
        private boolean isBestseller = false;
        private boolean isExclusive = false;
        private boolean requiresPrescription = false;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Analytics {
        private Integer views = 0;
        private Integer uniqueViews = 0;
        private Integer addedToCart = 0;
        private Integer purchased = 0;
        private Double conversionRate = 0.0;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SEO {
        private String metaTitle;
        private String metaDescription;
        private List<String> keywords = new ArrayList<>();
    }
}
