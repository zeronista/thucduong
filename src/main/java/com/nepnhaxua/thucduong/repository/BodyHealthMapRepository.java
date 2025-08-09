package com.nepnhaxua.thucduong.repository;

import com.nepnhaxua.thucduong.entity.BodyHealthMap;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BodyHealthMapRepository extends MongoRepository<BodyHealthMap, String> {
    
    Optional<BodyHealthMap> findByBodyPartAndIsActiveTrue(String bodyPart);
    
    List<BodyHealthMap> findByIsActiveTrueOrderByDisplayOrder();
    
    @Query("{ 'recommendations.products.productId': ?0, 'isActive': true }")
    List<BodyHealthMap> findByRecommendedProduct(String productId);
    
    @Query("{ 'healthInfo.commonIssues.symptoms': { $in: ?0 }, 'isActive': true }")
    List<BodyHealthMap> findBySymptoms(List<String> symptoms);
}
