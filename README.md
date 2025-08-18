# Nếp Nhà Xưa - E-commerce Platform for Macrobiotic Products

## Giới thiệu

Nếp Nhà Xưa là nền tảng thương mại điện tử chuyên cung cấp thực phẩm thực dưỡng chất lượng cao, phù hợp với mọi nhu cầu dinh dưỡng của gia đình Việt.

## Công nghệ sử dụng

### Backend

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security**
- **MongoDB** - Database
- **Redis** - Caching
- **Thymeleaf** - Template engine

### Frontend

- **HTML5/CSS3**
- **JavaScript (Vanilla)**
- **Thymeleaf** - Server-side rendering
- **Responsive Design** - Mobile-first approach

## Cấu trúc dự án

```
thucduong/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/nepnhaxua/thucduong/
│   │   │       ├── config/         # Configuration classes
│   │   │       ├── controller/     # REST & MVC Controllers
│   │   │       ├── entity/         # Domain entities
│   │   │       ├── repository/     # Data repositories
│   │   │       └── service/        # Business logic
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/           # Stylesheets
│   │       │   ├── js/            # JavaScript files
│   │       │   ├── images/        # Static images
│   │       │   └── fonts/         # Web fonts
│   │       └── templates/         # Thymeleaf templates
│   └── test/                      # Test files
└── pom.xml                        # Maven dependencies
```

## Cài đặt và chạy

### Yêu cầu

- JDK 17+
- Maven 3.6+
- MongoDB 4.4+
- Redis 6.0+ (optional for caching)

### Các bước cài đặt

1. **Clone repository**

```bash
git clone https://github.com/your-username/thucduong.git
cd thucduong
```

2. **Cấu hình MongoDB**

- Đảm bảo MongoDB đang chạy trên `localhost:27017`
- Hoặc cập nhật connection string trong `application.properties`

3. **Build project**

```bash
mvn clean install
```

4. **Chạy ứng dụng**

```bash
mvn spring-boot:run
```

5. **Truy cập**

- Application: http://localhost:8080
- API Documentation: http://localhost:8080/swagger-ui.html

## Các trang đã implement

### Public Pages

- **Trang chủ** (`/`) - Hero, sản phẩm nổi bật, blog preview
- **Sản phẩm** (`/products`) - Danh sách, filter, sort, search
- **Chi tiết sản phẩm** (`/products/{slug}`) - (In progress)
- **Giỏ hàng** (`/cart`) - (In progress)
- **Blog** (`/blog`) - (In progress)
- **Đăng nhập** (`/login`) - (In progress)

### API Endpoints

- `GET /api/products` - Danh sách sản phẩm
- `GET /api/products/{slug}` - Chi tiết sản phẩm
- `GET /api/products/featured` - Sản phẩm nổi bật
- `GET /api/health-map` - Body health mapping

## Tính năng chính

### Đã hoàn thành

- ✅ Responsive design
- ✅ Product listing với filters
- ✅ Search functionality
- ✅ Quick view modal
- ✅ Cart với localStorage
- ✅ Wishlist functionality
- ✅ Interactive body diagram
- ✅ Lazy loading images

### Đang phát triển

- 🚧 User authentication
- 🚧 Checkout process
- 🚧 Order management
- 🚧 Blog system
- 🚧 Admin panel

## Development

### Code Style

- Java: Google Java Style Guide
- JavaScript: ES6+ features
- CSS: BEM methodology với CSS Variables

### Testing

```bash
# Run all tests
mvn test

# Run with coverage
mvn test jacoco:report
```

### Building for Production

```bash
mvn clean package
java -jar target/thucduong-0.0.1-SNAPSHOT.jar
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License.

## Contact

- Website: [nepnhaxua.com.vn](https://nepnhaxua.com.vn)
- Email: cskh@nepnhaxua.com.vn
- Hotline: 093 796 00 88

## Acknowledgments

- Spring Boot team for the amazing framework
- MongoDB team for the flexible database
- All contributors and testers
