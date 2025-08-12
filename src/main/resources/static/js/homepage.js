// Homepage JavaScript

document.addEventListener('DOMContentLoaded', function() {
    initBodySection();
    initProductCards();
    initTestimonials();
    initAnimations();
});

// Body Section Interactivity
function initBodySection() {
    const bodyPoints = document.querySelectorAll('.body-point');
    const bodyBenefits = document.getElementById('bodyBenefits');
    
    const benefitsData = {
        brain: {
            title: 'Não bộ',
            benefits: [
                'Ginkgo biloba tăng cường tuần hoàn não',
                'Omega-3 từ hạt chia hỗ trợ trí nhớ',
                'Vitamin B complex nuôi dưỡng thần kinh'
            ]
        },
        heart: {
            title: 'Tim mạch',
            benefits: [
                'Yến mạch giảm cholesterol xấu',
                'Hạt lanh giàu omega-3 bảo vệ tim',
                'Nấm linh chi hỗ trợ ổn định huyết áp'
            ]
        },
        liver: {
            title: 'Gan',
            benefits: [
                'Nghệ tươi thanh lọc và bảo vệ gan',
                'Atiso hỗ trợ giải độc gan',
                'Táo đỏ tăng cường chức năng gan'
            ]
        },
        stomach: {
            title: 'Dạ dày',
            benefits: [
                'Gạo lứt dễ tiêu hóa, giàu chất xơ',
                'Nghệ đen bảo vệ niêm mạc dạ dày',
                'Men vi sinh cân bằng hệ tiêu hóa'
            ]
        },
        intestines: {
            title: 'Ruột',
            benefits: [
                'Bột yến mạch làm sạch ruột',
                'Hạt chia giàu chất xơ hòa tan',
                'Củ sen hỗ trợ nhu động ruột'
            ]
        },
        kidney: {
            title: 'Thận',
            benefits: [
                'Đậu đen bổ thận, lợi tiểu',
                'Hạt sen thanh nhiệt, an thần',
                'Nước dừa tươi thanh lọc thận'
            ]
        }
    };
    
    bodyPoints.forEach(point => {
        point.addEventListener('click', function() {
            const part = this.dataset.part;
            const data = benefitsData[part];
            
            if (data) {
                // Remove active state from all points
                bodyPoints.forEach(p => p.classList.remove('active'));
                this.classList.add('active');
                
                // Update benefits display
                bodyBenefits.innerHTML = `
                    <h4 class="benefits-title">${data.title}</h4>
                    <ul class="benefits-list">
                        ${data.benefits.map(benefit => `<li>${benefit}</li>`).join('')}
                    </ul>
                `;
                
                // Add animation
                bodyBenefits.style.opacity = '0';
                setTimeout(() => {
                    bodyBenefits.style.opacity = '1';
                }, 100);
            }
        });
    });
    
    // Add CSS for active state
    const style = document.createElement('style');
    style.textContent = `
        .body-point.active .point-dot {
            background-color: var(--accent-color);
            transform: scale(1.3);
        }
        
        .body-point.active .point-label {
            opacity: 1;
            visibility: visible;
        }
        
        .benefits-title {
            font-size: var(--fs-2xl);
            color: var(--primary-color);
            margin-bottom: var(--space-3);
        }
        
        .benefits-list {
            list-style: none;
            padding: 0;
        }
        
        .benefits-list li {
            padding: var(--space-2) 0;
            padding-left: var(--space-5);
            position: relative;
            color: var(--text-secondary);
            line-height: var(--lh-relaxed);
        }
        
        .benefits-list li::before {
            content: '✓';
            position: absolute;
            left: 0;
            color: var(--success-color);
            font-weight: bold;
        }
        
        #bodyBenefits {
            transition: opacity var(--transition-base);
        }
    `;
    document.head.appendChild(style);
    
    // Click first point by default
    if (bodyPoints.length > 0) {
        bodyPoints[0].click();
    }
}

// Product Cards Enhancement
function initProductCards() {
    const productCards = document.querySelectorAll('.product-card');
    
    productCards.forEach(card => {
        const addToCartBtn = card.querySelector('.add-to-cart');
        
        if (addToCartBtn) {
            // Prevent navigation when clicking add to cart
            addToCartBtn.addEventListener('click', function(e) {
                e.preventDefault();
                e.stopPropagation();
                
                // Trigger the global add to cart handler
                const event = new Event('click', { bubbles: true });
                this.dispatchEvent(event);
            });
        }
    });
}

// Testimonials Auto-scroll
function initTestimonials() {
    const testimonialsGrid = document.querySelector('.testimonials-grid');
    if (!testimonialsGrid) return;
    
    // Clone testimonials for infinite scroll effect
    const testimonials = Array.from(testimonialsGrid.children);
    if (testimonials.length < 4) {
        testimonials.forEach(testimonial => {
            const clone = testimonial.cloneNode(true);
            testimonialsGrid.appendChild(clone);
        });
    }
}

// Scroll Animations
function initAnimations() {
    const animatedElements = document.querySelectorAll(
        '.hero-content, .about-section, .product-card, .blog-card, .commitment-item, .testimonial-card'
    );
    
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };
    
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('fade-in');
                observer.unobserve(entry.target);
            }
        });
    }, observerOptions);
    
    // Add CSS for animations
    const style = document.createElement('style');
    style.textContent = `
        .hero-content,
        .about-section,
        .product-card,
        .blog-card,
        .commitment-item,
        .testimonial-card {
            opacity: 0;
            transform: translateY(20px);
            transition: opacity 0.6s ease, transform 0.6s ease;
        }
        
        .fade-in {
            opacity: 1;
            transform: translateY(0);
        }
        
        /* Stagger animations for grid items */
        .product-card:nth-child(1),
        .blog-card:nth-child(1),
        .commitment-item:nth-child(1),
        .testimonial-card:nth-child(1) {
            transition-delay: 0.1s;
        }
        
        .product-card:nth-child(2),
        .blog-card:nth-child(2),
        .commitment-item:nth-child(2),
        .testimonial-card:nth-child(2) {
            transition-delay: 0.2s;
        }
        
        .product-card:nth-child(3),
        .blog-card:nth-child(3),
        .commitment-item:nth-child(3),
        .testimonial-card:nth-child(3) {
            transition-delay: 0.3s;
        }
        
        .product-card:nth-child(4),
        .commitment-item:nth-child(4) {
            transition-delay: 0.4s;
        }
        
        .commitment-item:nth-child(5) {
            transition-delay: 0.5s;
        }
    `;
    document.head.appendChild(style);
    
    animatedElements.forEach(el => {
        observer.observe(el);
    });
}

// Smooth scroll for anchor links
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function(e) {
        e.preventDefault();
        const target = document.querySelector(this.getAttribute('href'));
        if (target) {
            target.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }
    });
});
