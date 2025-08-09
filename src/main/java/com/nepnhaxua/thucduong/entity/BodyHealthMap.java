package com.nepnhaxua.thucduong.entity;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bodyHealthMap")
public class BodyHealthMap {
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String bodyPart; // brain, heart, liver, stomach, intestines, kidneys, lungs
    
    private String code; // unique identifier for frontend
    
    private Map<String, String> displayName; // vi, en
    
    private Visualization visualization;
    private HealthInfo healthInfo;
    private Interactions interactions;
    private Recommendations recommendations;
    private Metadata metadata;
    
    private Integer displayOrder;
    private boolean isActive = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Nested classes
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Visualization {
        private String svgPath;
        private String imageMapCoords;
        private Position position;
        private Integer zIndex;
        private Animations animations;
        private List<Hotspot> hotspots = new ArrayList<>();
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Position {
        private Double x;
        private Double y;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Animations {
        private String hover;
        private String click;
        private String pulse; // for drawing attention
        private AnimationConfig config;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnimationConfig {
        private Integer duration; // milliseconds
        private String easing;
        private boolean loop;
        private Integer delay;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Hotspot {
        private Double x;
        private Double y;
        private Double radius;
        private String label;
        private String tooltipContent;
        private String action; // click action
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HealthInfo {
        private List<Function> primaryFunctions = new ArrayList<>();
        private List<CommonIssue> commonIssues = new ArrayList<>();
        private List<NutritionalNeed> nutritionalNeeds = new ArrayList<>();
        private MacrobioticPrinciples macrobioticPrinciples;
        private List<WarningSign> warningSignns = new ArrayList<>();
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Function {
        private Map<String, String> title; // vi, en
        private Map<String, String> description; // vi, en
        private String importance; // critical, major, minor
        private String iconClass;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommonIssue {
        private Map<String, String> name; // vi, en
        private List<String> symptoms;
        private List<String> causes;
        private List<String> prevention;
        private List<String> relatedProducts; // Product IDs
        private String severity; // mild, moderate, severe
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NutritionalNeed {
        private String nutrient;
        private String dailyRequirement;
        private List<String> benefits;
        private List<String> sources;
        private List<String> deficiencySymptoms;
        private RecommendedIntake recommendedIntake;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecommendedIntake {
        private Map<String, Double> byAge; // "18-30": 1000, "31-50": 1200
        private Map<String, Double> byGender; // "male": 1000, "female": 1200
        private Map<String, Double> byCondition; // "pregnancy": 1500, "lactation": 1300
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MacrobioticPrinciples {
        private String yinYangBalance;
        private Map<String, SeasonalFood> seasonalConsiderations;
        private List<String> recommendedCookingMethods;
        private List<String> avoidFoods;
        private Map<String, String> healingFoods; // food -> benefit
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SeasonalFood {
        private List<String> recommendedFoods;
        private List<String> avoidFoods;
        private String cookingStyle;
        private String energyType; // warming, cooling, neutral
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WarningSign {
        private String symptom;
        private String urgency; // immediate, soon, monitor
        private String action;
        private List<String> possibleCauses;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Interactions {
        private List<DidYouKnow> didYouKnowFacts = new ArrayList<>();
        private List<HealthTip> tips = new ArrayList<>();
        private List<Recipe> recipes = new ArrayList<>();
        private List<Exercise> exercises = new ArrayList<>();
        private Quiz quiz;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DidYouKnow {
        private Map<String, String> fact; // vi, en
        private String source;
        private List<String> relatedProducts; // Product IDs
        private String category; // nutrition, lifestyle, science
        private boolean isVerified;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HealthTip {
        private Map<String, String> title; // vi, en
        private Map<String, String> content; // vi, en
        private String category; // nutrition, lifestyle, exercise, prevention
        private String difficulty; // easy, medium, hard
        private List<String> tags;
        private String timing; // morning, afternoon, evening, anytime
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Recipe {
        private String name;
        private List<String> benefits;
        private List<String> ingredients;
        private Integer prepTime; // minutes
        private Integer cookTime; // minutes
        private String difficulty;
        private String blogPostId;
        private List<String> dietaryTags; // vegan, gluten-free, etc.
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Exercise {
        private String name;
        private String description;
        private String benefit;
        private Integer duration; // minutes
        private String intensity; // low, medium, high
        private List<String> precautions;
        private String videoUrl;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Quiz {
        private String title;
        private List<Question> questions = new ArrayList<>();
        private Integer passingScore;
        private String completionMessage;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Question {
        private String question;
        private List<String> options;
        private Integer correctAnswer;
        private String explanation;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Recommendations {
        private List<ProductRecommendation> products = new ArrayList<>();
        private List<Bundle> bundles = new ArrayList<>();
        private PersonalizationRules personalizationRules;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductRecommendation {
        private String productId;
        private String reason;
        private Integer priority;
        private Map<String, Object> conditions; // age range, gender, health conditions
        private String timing; // morning, with meals, before bed
        private String duration; // continuous, 1-month, seasonal
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Bundle {
        private String name;
        private List<String> products;
        private Double discount;
        private String description;
        private String benefit;
        private Integer duration; // days
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonalizationRules {
        private Map<String, List<String>> byHealthCondition; // condition -> product IDs
        private Map<String, List<String>> byAge; // age range -> product IDs
        private Map<String, List<String>> byLifestyle; // lifestyle -> product IDs
        private Map<String, Double> scoringWeights; // factor -> weight
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Metadata {
        private String medicalDisclaimer;
        private String lastReviewedBy;
        private LocalDateTime lastReviewedDate;
        private List<Reference> references = new ArrayList<>();
        private Map<String, String> certifications; // certification -> number
        private Integer version;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Reference {
        private String title;
        private String url;
        private String type; // research, article, book, study
        private String author;
        private Integer year;
        private String journal;
        private String doi;
    }
}
