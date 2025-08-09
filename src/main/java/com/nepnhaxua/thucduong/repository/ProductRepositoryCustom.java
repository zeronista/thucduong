package com.nepnhaxua.thucduong.repository;

import com.nepnhaxua.thucduong.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

// Custom repository interface for complex queries
public interface ProductRepositoryCustom {
    Page<Product> searchWithFilters(String searchTerm, String category,
                                    Double minPrice, Double maxPrice,
                                    List<String> tags, String sortBy,
                                    Pageable pageable);
    
    List<Product> findSimilarProducts(String productId, int limit);
    
    List<Product> getPersonalizedRecommendations(String userId, int limit);
}
