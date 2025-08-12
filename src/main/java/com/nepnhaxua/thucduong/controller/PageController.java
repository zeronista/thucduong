package com.nepnhaxua.thucduong.controller;

import com.nepnhaxua.thucduong.entity.Product;
import com.nepnhaxua.thucduong.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final ProductRepository productRepository;

    @GetMapping("/")
    public String homepage(Model model) {
        // Lấy sản phẩm nổi bật cho trang chủ
        List<Product> featuredProducts = productRepository
                .findByFlagsIsFeaturedTrueAndFlagsIsActiveTrueOrderByCreatedAtDesc()
                .stream()
                .limit(4)
                .toList();

        model.addAttribute("featuredProducts", featuredProducts);

        // TODO: Thêm tin tức mới nhất khi có BlogRepository
        // model.addAttribute("latestPosts",
        // blogRepository.findTop3ByOrderByCreatedAtDesc());

        return "homepage";
    }

    @GetMapping("/products")
    public String products(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String q,
            @RequestParam(required = false, defaultValue = "createdAt") String sort,
            @RequestParam(required = false, defaultValue = "DESC") Sort.Direction dir,
            Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        Page<Product> productPage;

        if (q != null && !q.isBlank()) {
            productPage = productRepository.searchProducts(q, pageable);
        } else if (category != null && !category.isBlank()) {
            // For category filter, use the category.main field
            productPage = productRepository.findByCategoryMainAndFlagsIsActiveTrue(category, pageable);
        } else {
            // For all products, we need to filter only active ones
            // Since there's no direct method, we'll use findAll for now
            // In production, you'd want to add a custom query for this
            productPage = productRepository.findAll(pageable);
        }

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("currentCategory", category);
        model.addAttribute("searchQuery", q);
        model.addAttribute("sortBy", sort);
        model.addAttribute("sortDirection", dir.toString());

        // TODO: Lấy danh sách categories khi có CategoryRepository
        // model.addAttribute("categories", categoryRepository.findAll());

        return "product";
    }

    @GetMapping("/products/{slug}")
    public String productDetail(@PathVariable String slug, Model model) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new java.util.NoSuchElementException("Product not found"));

        model.addAttribute("product", product);

        // Lấy sản phẩm liên quan (cùng category)
        List<Product> relatedProducts = productRepository
                .findByCategoryMainAndFlagsIsActiveTrue(product.getCategory().getMain(), PageRequest.of(0, 4))
                .stream()
                .filter(p -> !p.getId().equals(product.getId()))
                .limit(3)
                .toList();

        model.addAttribute("relatedProducts", relatedProducts);

        return "product_detail";
    }

    @GetMapping("/cart")
    public String cart() {
        return "cart";
    }

    @GetMapping("/checkout")
    public String checkout() {
        return "payment";
    }

    @GetMapping("/blog")
    public String blog(Model model) {
        // TODO: Implement khi có BlogRepository
        return "blog";
    }

    @GetMapping("/blog/{slug}")
    public String blogDetail(@PathVariable String slug, Model model) {
        // TODO: Implement khi có BlogRepository
        return "blog_detail";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
