package com.nepnhaxua.thucduong.service;

import com.nepnhaxua.thucduong.entity.BodyHealthMap;
import com.nepnhaxua.thucduong.entity.Product;
import com.nepnhaxua.thucduong.entity.User;
import com.nepnhaxua.thucduong.repository.BodyHealthMapRepository;
import com.nepnhaxua.thucduong.repository.ProductRepository;
import com.nepnhaxua.thucduong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BodyHealthMapService {
    
    private final BodyHealthMapRepository bodyHealthMapRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    
    /**
     * Get all active body parts for the health map
     */
    @Cacheable(value = "bodyHealthMap", key = "'all'")
    public List<BodyHealthMap> getAllBodyParts() {
        return bodyHealthMapRepository.findByIsActiveTrueOrderByDisplayOrder();
    }
    
    /**
     * Get detailed information for a specific body part
     */
    @Cacheable(value = "bodyHealthMap", key = "#bodyPart")
    public BodyHealthMap getBodyPartDetails(String bodyPart) {
        return bodyHealthMapRepository.findByBodyPartAndIsActiveTrue(bodyPart)
                .orElseThrow(() -> new NoSuchElementException("Body part not found: " + bodyPart));
    }
    
    /**
     * Get personalized recommendations for a body part based on user profile
     */
    @Transactional(readOnly = true)
    public PersonalizedRecommendations getPersonalizedRecommendations(
            String bodyPart, String userId, String language) {
        
        BodyHealthMap healthMap = getBodyPartDetails(bodyPart);
        User user = userId != null ? userRepository.findById(userId).orElse(null) : null;
        
        PersonalizedRecommendations recommendations = new PersonalizedRecommendations();
        recommendations.setBodyPart(bodyPart);
        recommendations.setLanguage(language);
        
        // Get base product recommendations
        List<String> productIds = healthMap.getRecommendations().getProducts().stream()
                .map(BodyHealthMap.ProductRecommendation::getProductId)
                .collect(Collectors.toList());
        
        List<Product> products = productRepository.findRelatedProducts(productIds);
        
        // Apply personalization if user is logged in
        if (user != null) {
            products = personalizeProducts(products, user, healthMap);
            recommendations.setPersonalized(true);
        }
        
        // Set recommendations
        recommendations.setProducts(products);
        recommendations.setBundles(getBundlesForBodyPart(healthMap, products));
        recommendations.setHealthTips(getHealthTipsForUser(healthMap, user, language));
        recommendations.setDidYouKnow(getRandomDidYouKnow(healthMap, language));
        
        return recommendations;
    }
    
    /**
     * Get interactive content for body part hover/click
     */
    public InteractiveContent getInteractiveContent(String bodyPart, String action, String language) {
        BodyHealthMap healthMap = getBodyPartDetails(bodyPart);
        InteractiveContent content = new InteractiveContent();
        
        switch (action) {
            case "hover":
                content.setType("tooltip");
                content.setContent(getTooltipContent(healthMap, language));
                break;
            case "click":
                content.setType("detailed");
                content.setContent(getDetailedContent(healthMap, language));
                break;
            case "quiz":
                content.setType("quiz");
                content.setContent(getQuizContent(healthMap, language));
                break;
            default:
                content.setType("basic");
                content.setContent(getBasicContent(healthMap, language));
        }
        
        return content;
    }
    
    /**
     * Track user interaction with body health map
     */
    @Transactional
    public void trackInteraction(String bodyPart, String userId, String action) {
        // Log interaction for analytics
        log.info("User {} performed {} on body part {}", userId, action, bodyPart);
        
        // Update user's recently viewed if applicable
        // If needed, implement recording in analytics later
        
        // Could also update analytics collection here
    }
    
    /**
     * Get health assessment based on user's symptoms
     */
    public HealthAssessment getHealthAssessment(String bodyPart, List<String> symptoms) {
        BodyHealthMap healthMap = getBodyPartDetails(bodyPart);
        HealthAssessment assessment = new HealthAssessment();
        
        // Match symptoms with common issues
        List<BodyHealthMap.CommonIssue> matchedIssues = healthMap.getHealthInfo()
                .getCommonIssues().stream()
                .filter(issue -> !Collections.disjoint(issue.getSymptoms(), symptoms))
                .collect(Collectors.toList());
        
        assessment.setPossibleIssues(matchedIssues);
        assessment.setRecommendedProducts(getProductsForIssues(matchedIssues));
        assessment.setUrgencyLevel(calculateUrgencyLevel(symptoms, healthMap));
        assessment.setPreventiveMeasures(getPreventiveMeasures(matchedIssues));
        
        return assessment;
    }
    
    // Helper methods
    
    private List<Product> personalizeProducts(List<Product> products, User user, BodyHealthMap healthMap) {
        // Score products based on user profile
        Map<String, Double> productScores = new HashMap<>();
        
        for (Product product : products) {
            double score = 1.0;
            
            // Age-based scoring
            if (user.getProfile() != null && user.getProfile().getDateOfBirth() != null) {
                // Calculate age and adjust score
                score *= getAgeRelevanceScore(product, user);
            }
            
            // Health condition scoring
            if (user.getProfile() != null && !user.getProfile().getHealthConditions().isEmpty()) {
                score *= getHealthConditionScore(product, user.getProfile().getHealthConditions());
            }
            
            // Dietary preference scoring
            if (user.getProfile() != null && !user.getProfile().getDietaryPreferences().isEmpty()) {
                score *= getDietaryPreferenceScore(product, user.getProfile().getDietaryPreferences());
            }
            
            // Purchase history scoring
            score *= getPurchaseHistoryScore(product, user);
            
            productScores.put(product.getId(), score);
        }
        
        // Sort products by score
        return products.stream()
                .sorted((p1, p2) -> Double.compare(
                        productScores.getOrDefault(p2.getId(), 0.0),
                        productScores.getOrDefault(p1.getId(), 0.0)))
                .collect(Collectors.toList());
    }
    
    private double getAgeRelevanceScore(Product product, User user) {
        // Implementation for age-based scoring
        return 1.0;
    }
    
    private double getHealthConditionScore(Product product, List<String> conditions) {
        // Implementation for health condition scoring
        return 1.0;
    }
    
    private double getDietaryPreferenceScore(Product product, List<String> preferences) {
        // Implementation for dietary preference scoring
        return 1.0;
    }
    
    private double getPurchaseHistoryScore(Product product, User user) {
        // Implementation for purchase history scoring
        return 1.0;
    }
    
    private List<Bundle> getBundlesForBodyPart(BodyHealthMap healthMap, List<Product> products) {
        // Implementation for getting relevant bundles
        return new ArrayList<>();
    }
    
    private List<HealthTip> getHealthTipsForUser(BodyHealthMap healthMap, User user, String language) {
        // Implementation for personalized health tips
        return new ArrayList<>();
    }
    
    private DidYouKnow getRandomDidYouKnow(BodyHealthMap healthMap, String language) {
        // Implementation for random did you know fact
        return new DidYouKnow();
    }
    
    private Map<String, Object> getTooltipContent(BodyHealthMap healthMap, String language) {
        Map<String, Object> content = new HashMap<>();
        content.put("title", healthMap.getDisplayName().get(language));
        content.put("primaryFunction", healthMap.getHealthInfo().getPrimaryFunctions().get(0));
        content.put("quickTip", "Click for more information");
        return content;
    }
    
    private Map<String, Object> getDetailedContent(BodyHealthMap healthMap, String language) {
        // Implementation for detailed content
        return new HashMap<>();
    }
    
    private Map<String, Object> getQuizContent(BodyHealthMap healthMap, String language) {
        // Implementation for quiz content
        return new HashMap<>();
    }
    
    private Map<String, Object> getBasicContent(BodyHealthMap healthMap, String language) {
        // Implementation for basic content
        return new HashMap<>();
    }
    
    private List<Product> getProductsForIssues(List<BodyHealthMap.CommonIssue> issues) {
        // Implementation for getting products for specific issues
        return new ArrayList<>();
    }
    
    private String calculateUrgencyLevel(List<String> symptoms, BodyHealthMap healthMap) {
        // Implementation for urgency calculation
        return "moderate";
    }
    
    private List<String> getPreventiveMeasures(List<BodyHealthMap.CommonIssue> issues) {
        // Implementation for preventive measures
        return new ArrayList<>();
    }
    
    // Response DTOs
    
    @lombok.Data
    public static class PersonalizedRecommendations {
        private String bodyPart;
        private String language;
        private boolean personalized;
        private List<Product> products;
        private List<Bundle> bundles;
        private List<HealthTip> healthTips;
        private DidYouKnow didYouKnow;
    }
    
    @lombok.Data
    public static class InteractiveContent {
        private String type;
        private Map<String, Object> content;
    }
    
    @lombok.Data
    public static class HealthAssessment {
        private List<BodyHealthMap.CommonIssue> possibleIssues;
        private List<Product> recommendedProducts;
        private String urgencyLevel;
        private List<String> preventiveMeasures;
    }
    
    @lombok.Data
    public static class Bundle {
        private String name;
        private List<Product> products;
        private Double discount;
        private String benefit;
    }
    
    @lombok.Data
    public static class HealthTip {
        private String title;
        private String content;
        private String category;
    }
    
    @lombok.Data
    public static class DidYouKnow {
        private String fact;
        private String source;
    }
}
