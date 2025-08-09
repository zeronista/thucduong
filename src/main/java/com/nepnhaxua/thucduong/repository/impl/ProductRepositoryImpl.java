package com.nepnhaxua.thucduong.repository.impl;

import com.nepnhaxua.thucduong.entity.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl {

    private final MongoTemplate mongoTemplate;

    public Page<Product> searchWithFilters(String searchTerm, String category,
                                           Double minPrice, Double maxPrice,
                                           List<String> tags, String sortBy,
                                           Pageable pageable) {
        List<AggregationOperation> operations = new ArrayList<>();

        Criteria criteria = new Criteria();
        criteria.and("flags.isActive").is(true);

        if (searchTerm != null && !searchTerm.isEmpty()) {
            criteria.and("$text").is(new Criteria().where("$search").is(searchTerm));
        }

        if (category != null && !category.isEmpty()) {
            criteria.and("category.main").is(category);
        }

        if (minPrice != null || maxPrice != null) {
            Criteria priceCriteria = new Criteria("pricing.regular");
            if (minPrice != null) priceCriteria.gte(minPrice);
            if (maxPrice != null) priceCriteria.lte(maxPrice);
            criteria.andOperator(priceCriteria);
        }

        if (tags != null && !tags.isEmpty()) {
            criteria.and("tags").in(tags);
        }

        operations.add(Aggregation.match(criteria));

        // Sorting
        Sort.Direction direction = Sort.Direction.DESC;
        String sortField = "createdAt";
        if (sortBy != null) {
            switch (sortBy) {
                case "price_asc":
                    sortField = "pricing.regular";
                    direction = Sort.Direction.ASC;
                    break;
                case "price_desc":
                    sortField = "pricing.regular";
                    break;
                case "rating":
                    sortField = "ratings.average";
                    break;
                case "popularity":
                    sortField = "analytics.purchased";
                    break;
                case "newest":
                    sortField = "createdAt";
                    break;
            }
        }

        operations.add(Aggregation.sort(Sort.by(direction, sortField)));

        List<AggregationOperation> countOps = new ArrayList<>(operations);
        countOps.add(Aggregation.count().as("total"));

        AggregationResults<CountResult> countResults = mongoTemplate.aggregate(
                Aggregation.newAggregation(countOps),
                "products",
                CountResult.class
        );

        long total = countResults.getMappedResults().isEmpty() ? 0 :
                countResults.getMappedResults().get(0).getTotal();

        operations.add(Aggregation.skip((long) pageable.getPageNumber() * pageable.getPageSize()));
        operations.add(Aggregation.limit(pageable.getPageSize()));

        AggregationResults<Product> results = mongoTemplate.aggregate(
                Aggregation.newAggregation(operations),
                "products",
                Product.class
        );

        return new PageImpl<>(results.getMappedResults(), pageable, total);
    }

    public List<Product> findSimilarProducts(String productId, int limit) {
        Product product = mongoTemplate.findById(productId, Product.class);
        if (product == null) {
            return new ArrayList<>();
        }

        Criteria criteria = new Criteria();
        criteria.and("_id").ne(productId);
        criteria.and("flags.isActive").is(true);
        criteria.and("category.main").is(product.getCategory().getMain());

        Double price = product.getPricing().getRegular();
        criteria.and("pricing.regular").gte(price * 0.7).lte(price * 1.3);

        Query query = new Query(criteria);
        query.limit(limit);
        query.with(Sort.by(Sort.Direction.DESC, "ratings.average"));

        return mongoTemplate.find(query, Product.class);
    }

    @Setter
    @Getter
    private static class CountResult {
        private long total;
    }
}
