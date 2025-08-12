// Product Page JavaScript

document.addEventListener('DOMContentLoaded', function() {
    initViewModes();
    initQuickView();
    initPriceFilter();
    initSortDropdown();
    initWishlist();
    initMobileFilters();
});

// View Modes (Grid/List)
function initViewModes() {
    const viewButtons = document.querySelectorAll('.view-mode');
    const productsGrid = document.getElementById('productsGrid');
    
    viewButtons.forEach(button => {
        button.addEventListener('click', function() {
            const view = this.dataset.view;
            
            // Update active button
            viewButtons.forEach(btn => btn.classList.remove('active'));
            this.classList.add('active');
            
            // Update grid class
            if (view === 'list') {
                productsGrid.classList.add('list-view');
            } else {
                productsGrid.classList.remove('list-view');
            }
            
            // Save preference
            localStorage.setItem('productViewMode', view);
        });
    });
    
    // Restore saved preference
    const savedView = localStorage.getItem('productViewMode');
    if (savedView === 'list') {
        document.querySelector('[data-view="list"]').click();
    }
}

// Quick View Modal
function initQuickView() {
    const modal = document.getElementById('quickViewModal');
    const modalBody = modal.querySelector('.modal-body');
    const modalClose = modal.querySelector('.modal-close');
    const quickViewButtons = document.querySelectorAll('.quick-view');
    
    quickViewButtons.forEach(button => {
        button.addEventListener('click', async function(e) {
            e.preventDefault();
            e.stopPropagation();
            
            const productId = this.dataset.productId;
            
            // Show loading state
            modal.classList.add('active');
            modalBody.innerHTML = '<div class="loading">Đang tải...</div>';
            
            try {
                // Fetch product details
                const response = await fetch(`/api/products/${productId}`);
                const data = await response.json();
                const product = data.data;
                
                // Render product details
                modalBody.innerHTML = `
                    <div class="quick-view-content">
                        <div class="quick-view-grid">
                            <div class="quick-view-images">
                                <img src="${product.media?.images[0]?.url || '/images/product-placeholder.jpg'}" 
                                     alt="${product.name}" class="main-image">
                            </div>
                            <div class="quick-view-info">
                                <h2 class="product-title">${product.name}</h2>
                                <div class="product-meta">
                                    <span class="product-sku">SKU: ${product.sku || 'N/A'}</span>
                                    ${product.ratings?.average ? `
                                        <div class="product-rating">
                                            <div class="stars">
                                                ${Array.from({length: 5}, (_, i) => 
                                                    `<span class="star ${i < Math.floor(product.ratings.average) ? 'filled' : ''}">★</span>`
                                                ).join('')}
                                            </div>
                                            <span class="rating-count">(${product.ratings.count} đánh giá)</span>
                                        </div>
                                    ` : ''}
                                </div>
                                <div class="product-price">
                                    <span class="price-current">${formatCurrency(product.pricing.sale || product.pricing.regular)}</span>
                                    ${product.pricing.sale ? `
                                        <span class="price-original">${formatCurrency(product.pricing.regular)}</span>
                                    ` : ''}
                                </div>
                                <div class="product-description">
                                    <p>${product.description?.short || ''}</p>
                                </div>
                                <div class="product-actions">
                                    <div class="quantity-selector">
                                        <button class="qty-btn minus">-</button>
                                        <input type="number" class="qty-input" value="1" min="1">
                                        <button class="qty-btn plus">+</button>
                                    </div>
                                    <button class="add-to-cart btn btn-primary"
                                            data-product-id="${product.id}"
                                            data-product-name="${product.name}"
                                            data-product-price="${product.pricing.sale || product.pricing.regular}">
                                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <circle cx="9" cy="21" r="1"></circle>
                                            <circle cx="20" cy="21" r="1"></circle>
                                            <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"></path>
                                        </svg>
                                        Thêm vào giỏ
                                    </button>
                                </div>
                                <div class="product-links">
                                    <a href="/products/${product.slug}" class="view-details">Xem chi tiết →</a>
                                </div>
                            </div>
                        </div>
                    </div>
                `;
                
                // Add CSS for quick view
                if (!document.querySelector('#quick-view-styles')) {
                    const style = document.createElement('style');
                    style.id = 'quick-view-styles';
                    style.textContent = `
                        .loading {
                            text-align: center;
                            padding: var(--space-7);
                            color: var(--text-secondary);
                        }
                        
                        .quick-view-grid {
                            display: grid;
                            grid-template-columns: 1fr 1fr;
                            gap: var(--space-6);
                        }
                        
                        .quick-view-images {
                            position: relative;
                        }
                        
                        .main-image {
                            width: 100%;
                            height: auto;
                            border-radius: var(--radius-lg);
                        }
                        
                        .product-title {
                            font-size: var(--fs-2xl);
                            margin-bottom: var(--space-3);
                        }
                        
                        .product-meta {
                            display: flex;
                            align-items: center;
                            gap: var(--space-4);
                            margin-bottom: var(--space-4);
                        }
                        
                        .product-sku {
                            font-size: var(--fs-sm);
                            color: var(--text-secondary);
                        }
                        
                        .product-description {
                            margin: var(--space-4) 0;
                            color: var(--text-secondary);
                            line-height: var(--lh-relaxed);
                        }
                        
                        .quantity-selector {
                            display: flex;
                            align-items: center;
                            border: 1px solid var(--border-color);
                            border-radius: var(--radius-base);
                            overflow: hidden;
                            width: fit-content;
                        }
                        
                        .qty-btn {
                            padding: var(--space-2) var(--space-3);
                            background: none;
                            border: none;
                            cursor: pointer;
                            font-size: var(--fs-lg);
                            color: var(--text-secondary);
                            transition: all var(--transition-fast);
                        }
                        
                        .qty-btn:hover {
                            background-color: var(--bg-secondary);
                        }
                        
                        .qty-input {
                            width: 60px;
                            text-align: center;
                            border: none;
                            border-left: 1px solid var(--border-color);
                            border-right: 1px solid var(--border-color);
                            padding: var(--space-2);
                        }
                        
                        .product-links {
                            margin-top: var(--space-4);
                            padding-top: var(--space-4);
                            border-top: 1px solid var(--border-light);
                        }
                        
                        .view-details {
                            color: var(--primary-color);
                            font-weight: var(--fw-medium);
                        }
                        
                        @media (max-width: 768px) {
                            .quick-view-grid {
                                grid-template-columns: 1fr;
                            }
                        }
                    `;
                    document.head.appendChild(style);
                }
                
                // Initialize quantity selector
                initQuantitySelector(modalBody);
                
            } catch (error) {
                modalBody.innerHTML = '<div class="error">Không thể tải thông tin sản phẩm</div>';
            }
        });
    });
    
    // Close modal
    modalClose.addEventListener('click', () => {
        modal.classList.remove('active');
    });
    
    modal.addEventListener('click', (e) => {
        if (e.target === modal) {
            modal.classList.remove('active');
        }
    });
}

// Quantity Selector
function initQuantitySelector(container) {
    const qtyInput = container.querySelector('.qty-input');
    const minusBtn = container.querySelector('.qty-btn.minus');
    const plusBtn = container.querySelector('.qty-btn.plus');
    
    if (!qtyInput || !minusBtn || !plusBtn) return;
    
    minusBtn.addEventListener('click', () => {
        const currentVal = parseInt(qtyInput.value);
        if (currentVal > 1) {
            qtyInput.value = currentVal - 1;
        }
    });
    
    plusBtn.addEventListener('click', () => {
        const currentVal = parseInt(qtyInput.value);
        qtyInput.value = currentVal + 1;
    });
    
    qtyInput.addEventListener('change', () => {
        if (qtyInput.value < 1) {
            qtyInput.value = 1;
        }
    });
}

// Price Filter
function initPriceFilter() {
    const priceInputs = document.querySelectorAll('.price-input');
    
    priceInputs.forEach(input => {
        input.addEventListener('input', function() {
            // Remove non-numeric characters
            this.value = this.value.replace(/[^0-9]/g, '');
        });
    });
}

// Sort Dropdown
function initSortDropdown() {
    const sortSelect = document.getElementById('sort');
    if (!sortSelect) return;
    
    // Update sort function is called inline in the HTML
    window.updateSort = function(value) {
        const url = new URL(window.location);
        url.searchParams.set('sort', value);
        window.location.href = url.toString();
    };
}

// Wishlist
function initWishlist() {
    const wishlistButtons = document.querySelectorAll('.add-to-wishlist');
    
    wishlistButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            
            const productId = this.dataset.productId;
            
            // Toggle wishlist state
            this.classList.toggle('active');
            
            // Save to localStorage
            let wishlist = JSON.parse(localStorage.getItem('wishlist') || '[]');
            const index = wishlist.indexOf(productId);
            
            if (index > -1) {
                wishlist.splice(index, 1);
                window.NepNhaXua.showNotification('Đã xóa khỏi yêu thích', 'info');
            } else {
                wishlist.push(productId);
                window.NepNhaXua.showNotification('Đã thêm vào yêu thích', 'success');
            }
            
            localStorage.setItem('wishlist', JSON.stringify(wishlist));
        });
        
        // Check if product is already in wishlist
        const productId = button.dataset.productId;
        const wishlist = JSON.parse(localStorage.getItem('wishlist') || '[]');
        if (wishlist.includes(productId)) {
            button.classList.add('active');
        }
    });
    
    // Add CSS for active state
    const style = document.createElement('style');
    style.textContent = `
        .add-to-wishlist.active {
            background-color: var(--danger-color);
            color: var(--text-white);
        }
        
        .add-to-wishlist.active svg {
            fill: currentColor;
        }
    `;
    document.head.appendChild(style);
}

// Mobile Filters
function initMobileFilters() {
    // Create filter toggle button for mobile
    const filterToggle = document.createElement('button');
    filterToggle.className = 'filter-toggle';
    filterToggle.innerHTML = `
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="4" y1="21" x2="4" y2="14"></line>
            <line x1="4" y1="10" x2="4" y2="3"></line>
            <line x1="12" y1="21" x2="12" y2="12"></line>
            <line x1="12" y1="8" x2="12" y2="3"></line>
            <line x1="20" y1="21" x2="20" y2="16"></line>
            <line x1="20" y1="12" x2="20" y2="3"></line>
            <circle cx="4" cy="14" r="2"></circle>
            <circle cx="12" cy="10" r="2"></circle>
            <circle cx="20" cy="14" r="2"></circle>
        </svg>
        Bộ lọc
    `;
    
    const mainContent = document.querySelector('.main-content');
    if (mainContent) {
        mainContent.insertBefore(filterToggle, mainContent.firstChild);
    }
    
    const sidebar = document.querySelector('.sidebar');
    
    filterToggle.addEventListener('click', () => {
        sidebar.classList.toggle('active');
    });
    
    // Add CSS for mobile filter toggle
    const style = document.createElement('style');
    style.textContent = `
        .filter-toggle {
            display: none;
            width: 100%;
            padding: var(--space-3) var(--space-4);
            background-color: var(--primary-color);
            color: var(--text-white);
            border: none;
            border-radius: var(--radius-base);
            font-size: var(--fs-base);
            font-weight: var(--fw-medium);
            cursor: pointer;
            margin-bottom: var(--space-4);
            gap: var(--space-2);
            align-items: center;
            justify-content: center;
        }
        
        @media (max-width: 992px) {
            .filter-toggle {
                display: flex;
            }
        }
    `;
    document.head.appendChild(style);
}

// Helper function to format currency
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    }).format(amount);
}
