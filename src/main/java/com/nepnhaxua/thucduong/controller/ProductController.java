package com.nepnhaxua.thucduong.controller;

import com.nepnhaxua.thucduong.api.ApiResponse;
import com.nepnhaxua.thucduong.entity.Product;
import com.nepnhaxua.thucduong.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Product>>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String q,
            @RequestParam(required = false, defaultValue = "createdAt") String sort,
            @RequestParam(required = false, defaultValue = "DESC") Sort.Direction dir
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        Page<Product> result;
        if (q != null && !q.isBlank()) {
            result = productRepository.searchProducts(q, pageable);
        } else if (category != null && !category.isBlank()) {
            result = productRepository.findByCategoryMainAndFlagsIsActiveTrue(category, pageable);
        } else {
            result = productRepository.findAll(pageable);
        }
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<Product>>> featured() {
        return ResponseEntity.ok(ApiResponse.ok(
                productRepository.findByFlagsIsFeaturedTrueAndFlagsIsActiveTrueOrderByCreatedAtDesc()
        ));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<Product>> get(@PathVariable String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new java.util.NoSuchElementException("Product not found"));
        return ResponseEntity.ok(ApiResponse.ok(product));
    }
}


