# Hướng Dẫn Triển Khai Hệ Thống Thực Dưỡng E-Commerce

## Tổng Quan
Đây là hướng dẫn chi tiết từng bước để triển khai hệ thống e-commerce thực dưỡng với MongoDB, Spring Boot và HTML/CSS/JS.

## Bước 1: Cài Đặt Môi Trường

### 1.1. Yêu cầu hệ thống
- Java 17 hoặc cao hơn
- MongoDB 6.0 hoặc cao hơn
- Redis 7.0 hoặc cao hơn
- Node.js 18+ (cho frontend)
- IntelliJ IDEA (khuyến nghị)

### 1.2. Cài đặt MongoDB
```bash
# Windows - Sử dụng MongoDB Installer từ mongodb.com

# Hoặc dùng Docker:
docker run -d --name mongodb \
  -p 27017:27017 \
  -e MONGO_INITDB_ROOT_USERNAME=admin \
  -e MONGO_INITDB_ROOT_PASSWORD=password \
  -v mongodb_data:/data/db \
  mongo:6.0
```

### 1.3. Cài đặt Redis
```bash
# Windows - Download từ Redis Windows port

# Hoặc dùng Docker:
docker run -d --name redis \
  -p 6379:6379 \
  redis:7-alpine
```

## Bước 2: Setup Project trong IntelliJ

### 2.1. Import Project
1. Mở IntelliJ IDEA
2. File → Open → Chọn thư mục project
3. Import as Maven project
4. Đợi IntelliJ index và download dependencies

### 2.2. Cấu hình Run Configuration
1. Run → Edit Configurations
2. Add New Configuration → Spring Boot
3. Main class: `com.nepnhaxua.thucduong.ThucduongApplication`
4. Environment variables:
   ```
   SPRING_PROFILES_ACTIVE=development
   JWT_SECRET=your-secret-key-here
   MAIL_USERNAME=your-email@gmail.com
   MAIL_PASSWORD=your-app-password
   ```

## Bước 3: Khởi tạo Database

### 3.1. Tạo Database và Collections
```javascript
// Chạy trong MongoDB Shell hoặc MongoDB Compass
use thucduong

// Tạo user cho application
db.createUser({
  user: "thucduong_user",
  pwd: "secure_password",
  roles: [
    { role: "readWrite", db: "thucduong" }
  ]
})

// Import initial data (optional)
// Sử dụng file src/main/resources/mongodb-schema/initial-data.js
```

### 3.2. Tạo Indexes
```javascript
// Chạy script tạo indexes
// File: src/main/resources/mongodb-schema/indexes-optimization.js
// Copy và paste vào MongoDB Shell
```

## Bước 4: Cấu hình Application

### 4.1. Update application.yml
```yaml
# Cập nhật connection string
spring:
  data:
    mongodb:
      uri: mongodb://thucduong_user:secure_password@localhost:27017/thucduong
```

### 4.2. Tạo file .env cho local development
```properties
# .env.local
MONGODB_URI=mongodb://localhost:27017/thucduong
REDIS_HOST=localhost
REDIS_PASSWORD=
JWT_SECRET=your-256-bit-secret-key
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-specific-password
```

## Bước 5: Chạy Application

### 5.1. Trong IntelliJ
1. Click vào nút Run (Shift+F10)
2. Hoặc: Right-click ThucduongApplication.java → Run

### 5.2. Verify Application
```bash
# Check health endpoint
curl http://localhost:8080/api/actuator/health

# Check API documentation
# Mở browser: http://localhost:8080/api/swagger-ui.html
```

## Bước 6: Tạo Sample Data

### 6.1. Tạo Admin User
```bash
# POST request to create admin
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@thucduong.com",
    "password": "Admin@123",
    "fullName": "Administrator",
    "role": "admin"
  }'
```

### 6.2. Import Sample Products
Sử dụng file `src/main/resources/sample-data/products.json`

## Bước 7: Setup Frontend

### 7.1. Cấu trúc thư mục Frontend
```
frontend/
├── index.html
├── css/
│   ├── main.css
│   ├── components/
│   └── pages/
├── js/
│   ├── app.js
│   ├── api/
│   ├── components/
│   └── utils/
├── images/
└── assets/
```

### 7.2. Tích hợp với Backend API
```javascript
// js/api/config.js
const API_BASE_URL = 'http://localhost:8080/api';

// js/api/products.js
async function getProducts(params) {
  const response = await fetch(`${API_BASE_URL}/products?${new URLSearchParams(params)}`);
  return response.json();
}
```

## Bước 8: Testing

### 8.1. Unit Tests
```bash
# Trong IntelliJ
# Right-click test folder → Run All Tests

# Hoặc dùng Maven
./mvnw test
```

### 8.2. Integration Tests
```bash
# Run specific test class
./mvnw test -Dtest=ProductRepositoryTest
```

## Bước 9: Monitoring & Logging

### 9.1. Check Logs
```bash
# Logs được lưu trong logs/thucduong.log
tail -f logs/thucduong.log
```

### 9.2. Monitor với Actuator
```bash
# Metrics
curl http://localhost:8080/api/actuator/metrics

# Prometheus metrics
curl http://localhost:8080/api/actuator/prometheus
```

## Bước 10: Deployment Preparation

### 10.1. Build cho Production
```bash
# Build JAR file
./mvnw clean package -Pproduction

# Output: target/thucduong-0.0.1-SNAPSHOT.jar
```

### 10.2. Environment Variables cho Production
```bash
export SPRING_PROFILES_ACTIVE=production
export MONGODB_URI=mongodb://production-server/thucduong
export REDIS_HOST=redis-production-server
export JWT_SECRET=production-secret-key
```

## Troubleshooting

### Issue 1: MongoDB Connection Failed
```
Solution:
1. Kiểm tra MongoDB đang chạy: `mongosh --eval "db.version()"`
2. Verify connection string trong application.yml
3. Check firewall settings
```

### Issue 2: Redis Connection Failed
```
Solution:
1. Kiểm tra Redis: `redis-cli ping`
2. Verify Redis configuration
3. Disable Redis temporarily: set spring.cache.type=none
```

### Issue 3: Port Already in Use
```
Solution:
1. Change port in application.yml: server.port=8081
2. Or kill process: netstat -ano | findstr :8080
```

## Best Practices

### 1. Security
- Luôn sử dụng HTTPS trong production
- Rotate JWT secret keys định kỳ
- Enable MongoDB authentication
- Implement rate limiting

### 2. Performance
- Enable MongoDB connection pooling
- Use Redis caching effectively
- Implement pagination for all list endpoints
- Use indexes cho các queries phổ biến

### 3. Monitoring
- Setup ELK stack cho centralized logging
- Use Prometheus + Grafana cho metrics
- Implement health checks
- Setup alerts cho critical issues

## Next Steps

1. **Frontend Development**: Phát triển giao diện người dùng
2. **Payment Integration**: Tích hợp VNPay, Momo
3. **Email Templates**: Tạo email templates đẹp
4. **SEO Optimization**: Optimize cho search engines
5. **Performance Testing**: Load testing với JMeter
6. **Security Audit**: Penetration testing
7. **CI/CD Pipeline**: Setup Jenkins hoặc GitLab CI

## Support Resources

- MongoDB Documentation: https://docs.mongodb.com/
- Spring Boot Documentation: https://spring.io/projects/spring-boot
- Project Issues: [Create issue on GitHub]
- Community Forum: [Link to forum]

---

**Note**: Hướng dẫn này được thiết kế cho môi trường development. Cho production deployment, cần thêm các bước về security hardening, scaling, và monitoring.
