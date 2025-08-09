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

import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chatSessions")
@CompoundIndexes({
    @CompoundIndex(name = "user_last_message_idx", def = "{'userId': 1, 'lastMessageAt': -1}"),
    @CompoundIndex(name = "session_created_idx", def = "{'sessionId': 1, 'createdAt': -1}")
})
public class ChatSession {
    @Id
    private String id;
    
    @Indexed
    private String sessionId;
    
    @Indexed
    private String userId; // optional for logged-in users
    
    private List<Message> messages = new ArrayList<>();
    private Context context;
    private UserProfile userProfile; // inferred or provided user info
    private ConversationMetrics metrics;
    private List<ActionItem> actionItems = new ArrayList<>();
    
    @Indexed
    private LocalDateTime createdAt;
    @Indexed
    private LocalDateTime lastMessageAt;
    private LocalDateTime closedAt;
    
    private String status; // active, completed, abandoned
    private Double satisfactionRating;
    private String feedback;
    
    // TTL index on createdAt for automatic cleanup
    @Indexed(expireAfterSeconds = 2592000) // 30 days
    private LocalDateTime expiresAt;
    
    // Nested classes
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        private String role; // user, assistant, system
        private String content;
        private LocalDateTime timestamp;
        private MessageType type; // text, product_recommendation, health_advice, recipe
        private List<String> productRecommendations = new ArrayList<>();
        private Map<String, Object> metadata = new HashMap<>();
        private Sentiment sentiment;
        private List<Intent> intents = new ArrayList<>();
        private boolean isEdited;
        private String editedContent;
        private LocalDateTime editedAt;
    }
    
    public enum MessageType {
        TEXT,
        PRODUCT_RECOMMENDATION,
        HEALTH_ADVICE,
        RECIPE,
        ORDER_INQUIRY,
        FAQ,
        IMAGE,
        VOICE_NOTE
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Context {
        private String primaryIntent; // product_inquiry, health_advice, recipe, order_support
        private List<String> mentionedBodyParts = new ArrayList<>();
        private List<String> mentionedCategories = new ArrayList<>();
        private List<String> mentionedProducts = new ArrayList<>();
        private List<String> healthConditions = new ArrayList<>();
        private List<String> dietaryPreferences = new ArrayList<>();
        private Map<String, Object> conversationState = new HashMap<>();
        private PriceRange pricePreference;
        private List<String> previousTopics = new ArrayList<>();
        private String currentTopic;
        private Integer conversationDepth = 0;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceRange {
        private Double min;
        private Double max;
        private String currency = "VND";
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserProfile {
        private String inferredAge;
        private String inferredGender;
        private List<String> interests = new ArrayList<>();
        private List<String> concerns = new ArrayList<>();
        private String experienceLevel; // beginner, intermediate, expert
        private Map<String, Integer> topicFrequency = new HashMap<>();
        private String preferredCommunicationStyle; // formal, casual, friendly
        private String language = "vi";
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConversationMetrics {
        private Integer messageCount = 0;
        private Integer userMessageCount = 0;
        private Integer assistantMessageCount = 0;
        private Double averageResponseTime; // seconds
        private Integer productsMentioned = 0;
        private Integer productsRecommended = 0;
        private Integer questionsAsked = 0;
        private Integer questionsAnswered = 0;
        private Map<String, Integer> intentCounts = new HashMap<>();
        private List<String> topKeywords = new ArrayList<>();
        private Double engagementScore;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Sentiment {
        private String overall; // positive, neutral, negative
        private Double score; // -1.0 to 1.0
        private Map<String, Double> emotions; // joy, trust, fear, surprise, sadness, disgust, anger, anticipation
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Intent {
        private String name;
        private Double confidence;
        private Map<String, String> entities = new HashMap<>();
        private String action; // search_product, show_recipe, health_advice, etc.
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActionItem {
        private String type; // add_to_cart, view_product, save_recipe, schedule_reminder
        private Map<String, Object> parameters = new HashMap<>();
        private LocalDateTime createdAt;
        private boolean completed;
        private LocalDateTime completedAt;
        private String result;
    }
}

// Separate entity for AI model configurations
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "aiConfigurations")
class AIConfiguration {
    @Id
    private String id;
    
    private String modelName;
    private String modelVersion;
    private ModelParameters parameters;
    private List<SystemPrompt> systemPrompts = new ArrayList<>();
    private KnowledgeBase knowledgeBase;
    private ResponseTemplates responseTemplates;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModelParameters {
        private Double temperature = 0.7;
        private Integer maxTokens = 1000;
        private Double topP = 0.9;
        private Double frequencyPenalty = 0.0;
        private Double presencePenalty = 0.0;
        private List<String> stopSequences = new ArrayList<>();
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SystemPrompt {
        private String role;
        private String content;
        private String language;
        private String useCase; // general, product_recommendation, health_advice
        private boolean isActive;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KnowledgeBase {
        private List<String> productCategories = new ArrayList<>();
        private Map<String, List<String>> healthBenefits = new HashMap<>();
        private Map<String, List<String>> commonQuestions = new HashMap<>();
        private List<String> disclaimers = new ArrayList<>();
        private Map<String, String> terminology = new HashMap<>();
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseTemplates {
        private Map<String, List<String>> greetings = new HashMap<>();
        private Map<String, List<String>> productRecommendations = new HashMap<>();
        private Map<String, List<String>> healthAdvice = new HashMap<>();
        private Map<String, List<String>> closings = new HashMap<>();
        private Map<String, String> errorMessages = new HashMap<>();
    }
}

// Entity for storing conversation analytics
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chatAnalytics")
class ChatAnalytics {
    @Id
    private String id;
    
    private String sessionId;
    private String userId;
    private LocalDateTime date;
    
    private ConversationAnalysis analysis;
    private List<TopicTrend> topicTrends = new ArrayList<>();
    private UserBehavior userBehavior;
    private BusinessMetrics businessMetrics;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConversationAnalysis {
        private Double overallSentiment;
        private Map<String, Integer> topicDistribution = new HashMap<>();
        private List<String> keyPhrases = new ArrayList<>();
        private Double customerSatisfactionScore;
        private List<String> unhandledQueries = new ArrayList<>();
        private Map<String, Double> intentAccuracy = new HashMap<>();
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopicTrend {
        private String topic;
        private Integer mentions;
        private Double sentimentScore;
        private List<String> relatedProducts = new ArrayList<>();
        private String trend; // increasing, stable, decreasing
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserBehavior {
        private Double engagementRate;
        private Integer averageMessagesPerSession;
        private Map<String, Integer> preferredInteractionTimes = new HashMap<>();
        private List<String> abandonmentReasons = new ArrayList<>();
        private Double returnRate;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BusinessMetrics {
        private Integer productsViewed;
        private Integer productsAddedToCart;
        private Double conversionRate;
        private Double averageOrderValue;
        private List<String> topRecommendedProducts = new ArrayList<>();
        private Map<String, Double> productConversionRates = new HashMap<>();
    }
}
