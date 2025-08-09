// Initial Data Script for Thuc Duong E-commerce
// Run this script in MongoDB shell or MongoDB Compass

// Switch to database
use thucduong;

// Clear existing data (BE CAREFUL IN PRODUCTION!)
db.users.deleteMany({});
db.products.deleteMany({});
db.categories.deleteMany({});
db.bodyHealthMap.deleteMany({});

// ==========================================
// 1. INSERT CATEGORIES
// ==========================================
db.categories.insertMany([
  {
    _id: ObjectId(),
    name: "Gạo lứt",
    slug: "gao-lut",
    type: "product",
    description: "Các loại gạo lứt giàu dinh dưỡng",
    image: "/images/categories/gao-lut.jpg",
    displayOrder: 1,
    isActive: true,
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    _id: ObjectId(),
    name: "Bún lứt",
    slug: "bun-lut",
    type: "product",
    description: "Bún lứt thơm ngon, tốt cho sức khỏe",
    image: "/images/categories/bun-lut.jpg",
    displayOrder: 2,
    isActive: true,
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    _id: ObjectId(),
    name: "Trà thảo mộc",
    slug: "tra-thao-moc",
    type: "product",
    description: "Trà thảo mộc tự nhiên",
    image: "/images/categories/tra-thao-moc.jpg",
    displayOrder: 3,
    isActive: true,
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    _id: ObjectId(),
    name: "Ngũ cốc",
    slug: "ngu-coc",
    type: "product",
    description: "Ngũ cốc dinh dưỡng",
    image: "/images/categories/ngu-coc.jpg",
    displayOrder: 4,
    isActive: true,
    createdAt: new Date(),
    updatedAt: new Date()
  }
]);

// ==========================================
// 2. INSERT SAMPLE PRODUCTS
// ==========================================
db.products.insertMany([
  {
    _id: ObjectId(),
    name: "Gạo lứt Điện Biên hữu cơ",
    slug: "gao-lut-dien-bien-huu-co",
    sku: "GL001",
    category: {
      main: "Gạo lứt",
      sub: "Gạo lứt hữu cơ",
      tags: ["hữu cơ", "Điện Biên"]
    },
    pricing: {
      cost: 35000,
      regular: 55000,
      sale: 49000,
      bulkPricing: [
        { minQuantity: 5, price: 47000 },
        { minQuantity: 10, price: 45000 }
      ],
      currency: "VND",
      taxable: true
    },
    media: {
      images: [
        {
          url: "/images/products/gao-lut-dien-bien-1.jpg",
          alt: "Gạo lứt Điện Biên hữu cơ",
          isPrimary: true,
          order: 1
        }
      ]
    },
    description: {
      shortDesc: "Gạo lứt Điện Biên được trồng hoàn toàn hữu cơ, không sử dụng phân bón hóa học",
      detailed: "Gạo lứt Điện Biên hữu cơ được trồng tại vùng núi cao Điện Biên, với khí hậu mát mẻ và đất đai màu mỡ. Sản phẩm được chứng nhận hữu cơ, không sử dụng phân bón hóa học hay thuốc trừ sâu. Gạo lứt giữ nguyên lớp cám giàu dinh dưỡng, chứa nhiều vitamin B, vitamin E, chất xơ và khoáng chất.",
      usage: "Vo nhẹ 1-2 lần, ngâm 30 phút trước khi nấu. Tỷ lệ 1 gạo : 2 nước",
      storage: "Bảo quản nơi khô ráo, thoáng mát. Tránh ánh nắng trực tiếp",
      warnings: "Không sử dụng nếu sản phẩm có mùi ẩm mốc"
    },
    ingredients: [
      {
        name: "Gạo lứt Điện Biên",
        percentage: 100,
        benefits: ["Giàu chất xơ", "Nhiều vitamin B", "Chứa khoáng chất"],
        isOrganic: true,
        allergen: false
      }
    ],
    nutritionalInfo: {
      servingSize: "100g",
      servingsPerPackage: 10,
      calories: 350,
      nutrients: {
        protein: { amount: 7.5, unit: "g", dailyValue: 15 },
        carbs: { amount: 76, unit: "g", dailyValue: 25 },
        fat: { amount: 2.7, unit: "g", dailyValue: 4 },
        fiber: { amount: 3.5, unit: "g", dailyValue: 14 }
      }
    },
    healthBenefits: [
      {
        bodyPart: "digestive",
        benefit: "Hỗ trợ tiêu hóa",
        description: "Chất xơ trong gạo lứt giúp cải thiện nhu động ruột",
        scientificEvidence: "Nhiều nghiên cứu chứng minh chất xơ giúp cải thiện tiêu hóa"
      },
      {
        bodyPart: "heart",
        benefit: "Tốt cho tim mạch",
        description: "Giúp giảm cholesterol xấu, bảo vệ tim mạch",
        scientificEvidence: "Nghiên cứu cho thấy gạo lứt giúp giảm nguy cơ bệnh tim"
      }
    ],
    certifications: [
      {
        name: "Organic",
        certifier: "USDA",
        certificateNumber: "VN-BIO-149",
        validUntil: new Date("2025-12-31")
      }
    ],
    inventory: {
      quantity: 500,
      reserved: 20,
      unit: "kg",
      lowStockThreshold: 50,
      trackInventory: true
    },
    manufacturer: {
      name: "HTX Nông nghiệp Điện Biên",
      origin: {
        country: "Việt Nam",
        region: "Điện Biên"
      }
    },
    ratings: {
      average: 4.8,
      count: 125,
      distribution: { 5: 100, 4: 20, 3: 5, 2: 0, 1: 0 }
    },
    tags: ["gạo lứt", "hữu cơ", "Điện Biên", "healthy", "organic"],
    flags: {
      isActive: true,
      isFeatured: true,
      isNew: false,
      isBestseller: true
    },
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    _id: ObjectId(),
    name: "Trà atiso Đà Lạt",
    slug: "tra-atiso-da-lat",
    sku: "TR001",
    category: {
      main: "Trà thảo mộc",
      sub: "Trà atiso",
      tags: ["Đà Lạt", "detox"]
    },
    pricing: {
      cost: 45000,
      regular: 75000,
      sale: 69000,
      currency: "VND",
      taxable: true
    },
    media: {
      images: [
        {
          url: "/images/products/tra-atiso-1.jpg",
          alt: "Trà atiso Đà Lạt",
          isPrimary: true,
          order: 1
        }
      ]
    },
    description: {
      shortDesc: "Trà atiso Đà Lạt nguyên chất, giúp thanh nhiệt, giải độc gan",
      detailed: "Trà atiso được làm từ lá và thân cây atiso trồng tại Đà Lạt. Sản phẩm được sấy khô tự nhiên, giữ nguyên dưỡng chất. Atiso có tác dụng thanh nhiệt, giải độc gan, hỗ trợ tiêu hóa và giúp giảm cholesterol.",
      usage: "Cho 1-2 túi trà vào cốc, đổ nước sôi 90-95°C, ngâm 5-7 phút",
      storage: "Bảo quản nơi khô ráo, tránh ánh sáng"
    },
    ingredients: [
      {
        name: "Lá atiso Đà Lạt",
        percentage: 100,
        benefits: ["Thanh nhiệt", "Giải độc gan", "Hỗ trợ tiêu hóa"],
        isOrganic: true,
        allergen: false
      }
    ],
    healthBenefits: [
      {
        bodyPart: "liver",
        benefit: "Bảo vệ gan",
        description: "Cynarin trong atiso giúp bảo vệ và phục hồi chức năng gan"
      }
    ],
    inventory: {
      quantity: 200,
      unit: "hộp",
      lowStockThreshold: 30
    },
    ratings: {
      average: 4.7,
      count: 89
    },
    tags: ["trà", "atiso", "Đà Lạt", "detox", "gan"],
    flags: {
      isActive: true,
      isFeatured: true
    },
    createdAt: new Date(),
    updatedAt: new Date()
  }
]);

// ==========================================
// 3. INSERT BODY HEALTH MAP DATA
// ==========================================
db.bodyHealthMap.insertMany([
  {
    _id: ObjectId(),
    bodyPart: "liver",
    code: "LIVER",
    displayName: {
      vi: "Gan",
      en: "Liver"
    },
    visualization: {
      svgPath: "M100,200 Q150,180 200,200 L200,250 Q150,270 100,250 Z",
      position: { x: 150, y: 225 },
      zIndex: 10,
      animations: {
        hover: "pulse",
        click: "zoom"
      }
    },
    healthInfo: {
      primaryFunctions: [
        {
          title: { vi: "Giải độc", en: "Detoxification" },
          description: { vi: "Gan giúp lọc và loại bỏ độc tố", en: "Liver filters and removes toxins" },
          importance: "critical"
        }
      ],
      commonIssues: [
        {
          name: { vi: "Gan nhiễm mỡ", en: "Fatty liver" },
          symptoms: ["Mệt mỏi", "Đau bụng phải", "Da vàng"],
          causes: ["Ăn nhiều dầu mỡ", "Uống rượu bia", "Béo phì"],
          prevention: ["Ăn uống lành mạnh", "Tập thể dục", "Hạn chế rượu bia"],
          relatedProducts: [],
          severity: "moderate"
        }
      ],
      nutritionalNeeds: [
        {
          nutrient: "Vitamin E",
          dailyRequirement: "15mg",
          benefits: ["Chống oxy hóa", "Bảo vệ tế bào gan"],
          sources: ["Hạt hướng dương", "Dầu olive", "Bơ"],
          deficiencySymptoms: ["Yếu cơ", "Thị lực kém"]
        }
      ]
    },
    interactions: {
      didYouKnowFacts: [
        {
          fact: {
            vi: "Gan có thể tự tái tạo đến 75% mô của nó",
            en: "The liver can regenerate up to 75% of its tissue"
          },
          source: "Medical Research Journal",
          category: "science"
        }
      ],
      tips: [
        {
          title: { vi: "Uống đủ nước", en: "Stay hydrated" },
          content: { vi: "Uống 2-3 lít nước mỗi ngày giúp gan hoạt động tốt", en: "Drink 2-3 liters daily" },
          category: "lifestyle",
          difficulty: "easy"
        }
      ]
    },
    displayOrder: 1,
    isActive: true,
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    _id: ObjectId(),
    bodyPart: "heart",
    code: "HEART",
    displayName: {
      vi: "Tim",
      en: "Heart"
    },
    visualization: {
      svgPath: "M150,200 C150,180 170,160 190,160 C210,160 230,180 230,200 C230,220 190,260 190,260 C190,260 150,220 150,200 Z",
      position: { x: 190, y: 210 },
      zIndex: 15,
      animations: {
        hover: "heartbeat",
        click: "zoom"
      }
    },
    healthInfo: {
      primaryFunctions: [
        {
          title: { vi: "Bơm máu", en: "Pump blood" },
          description: { vi: "Tim bơm máu đi khắp cơ thể", en: "Heart pumps blood throughout the body" },
          importance: "critical"
        }
      ]
    },
    displayOrder: 2,
    isActive: true,
    createdAt: new Date(),
    updatedAt: new Date()
  }
]);

// ==========================================
// 4. INSERT SAMPLE USERS
// ==========================================
db.users.insertOne({
  _id: ObjectId(),
  email: "admin@thucduong.com",
  password: "$2a$10$YourHashedPasswordHere", // Use bcrypt to hash
  fullName: "Administrator",
  phoneNumber: "0901234567",
  role: "admin",
  profile: {
    gender: "male",
    dietaryPreferences: ["organic"],
    allergies: []
  },
  addresses: [
    {
      type: "home",
      recipientName: "Administrator",
      recipientPhone: "0901234567",
      street: "123 Nguyễn Huệ",
      ward: "Bến Nghé",
      district: "Quận 1",
      city: "Hồ Chí Minh",
      isDefault: true
    }
  ],
  loyalty: {
    points: 1000,
    tier: "gold",
    lifetimeValue: 5000000
  },
  preferences: {
    newsletter: true,
    smsNotifications: true,
    language: "vi",
    currency: "VND"
  },
  isActive: true,
  emailVerified: true,
  createdAt: new Date(),
  updatedAt: new Date()
});

// ==========================================
// 5. CREATE SAMPLE DISCOUNT CODES
// ==========================================
db.discountCodes.insertMany([
  {
    _id: ObjectId(),
    code: "WELCOME10",
    description: "Giảm 10% cho khách hàng mới",
    type: "percentage",
    value: 10,
    minimumPurchase: 200000,
    maximumDiscount: 50000,
    applicableProducts: [],
    applicableCategories: [],
    usageLimit: 1000,
    usageCount: 0,
    userLimit: 1,
    startDate: new Date(),
    endDate: new Date("2024-12-31"),
    isActive: true,
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    _id: ObjectId(),
    code: "HEALTHY2024",
    description: "Giảm 50k cho đơn hàng từ 500k",
    type: "fixed",
    value: 50000,
    minimumPurchase: 500000,
    applicableProducts: [],
    applicableCategories: ["Gạo lứt", "Ngũ cốc"],
    usageLimit: 500,
    usageCount: 0,
    userLimit: 2,
    startDate: new Date(),
    endDate: new Date("2024-06-30"),
    isActive: true,
    createdAt: new Date(),
    updatedAt: new Date()
  }
]);

print("✅ Initial data inserted successfully!");
print("📊 Summary:");
print("- Categories: " + db.categories.countDocuments());
print("- Products: " + db.products.countDocuments());
print("- Body Health Map: " + db.bodyHealthMap.countDocuments());
print("- Users: " + db.users.countDocuments());
print("- Discount Codes: " + db.discountCodes.countDocuments());
