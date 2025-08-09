// MongoDB Indexes and Performance Optimization

// ==========================================
// COMPOUND INDEXES FOR COMMON QUERIES
// ==========================================

// 1. Users Collection Indexes
db.users.createIndex({ "email": 1 }, { unique: true, sparse: true });
db.users.createIndex({ "phoneNumber": 1 }, { unique: true, sparse: true });
db.users.createIndex({ "role": 1, "isActive": 1 });
db.users.createIndex({ "loyalty.tier": 1, "loyalty.points": -1 });
db.users.createIndex({ "createdAt": -1 });
db.users.createIndex({ "activity.lastLogin": -1 });
db.users.createIndex({ "addresses.city": 1, "addresses.district": 1 });
// Text search index for customer search
db.users.createIndex({ 
  "fullName": "text", 
  "email": "text", 
  "phoneNumber": "text" 
}, {
  weights: {
    fullName: 10,
    email: 5,
    phoneNumber: 5
  }
});

// 2. Products Collection Indexes
db.products.createIndex({ "slug": 1 }, { unique: true });
db.products.createIndex({ "sku": 1 }, { unique: true, sparse: true });
db.products.createIndex({ "category.main": 1, "flags.isActive": 1, "pricing.regular": 1 });
db.products.createIndex({ "category.main": 1, "category.sub": 1, "flags.isActive": 1 });
db.products.createIndex({ "tags": 1, "flags.isActive": 1 });
db.products.createIndex({ "ratings.average": -1, "ratings.count": -1 });
db.products.createIndex({ "flags.isFeatured": 1, "flags.isActive": 1, "createdAt": -1 });
db.products.createIndex({ "inventory.quantity": 1, "flags.isActive": 1 });
db.products.createIndex({ "pricing.sale": 1, "flags.isActive": 1 }, { sparse: true });
db.products.createIndex({ "healthBenefits.bodyPart": 1, "flags.isActive": 1 });
// Full text search index
db.products.createIndex({ 
  "name": "text", 
  "description.short": "text", 
  "description.detailed": "text",
  "ingredients.name": "text",
  "tags": "text",
  "searchKeywords": "text"
}, {
  weights: {
    name: 10,
    searchKeywords: 8,
    tags: 5,
    "description.short": 3,
    "ingredients.name": 3,
    "description.detailed": 1
  },
  default_language: "vietnamese"
});

// 3. Orders Collection Indexes
db.orders.createIndex({ "orderNumber": 1 }, { unique: true });
db.orders.createIndex({ "userId": 1, "createdAt": -1 });
db.orders.createIndex({ "status": 1, "createdAt": -1 });
db.orders.createIndex({ "payment.status": 1, "payment.method": 1 });
db.orders.createIndex({ "items.productId": 1 });
db.orders.createIndex({ "createdAt": -1 });
db.orders.createIndex({ "shippingAddress.city": 1, "status": 1 });
// For admin dashboard
db.orders.createIndex({ 
  "status": 1, 
  "payment.status": 1, 
  "createdAt": -1 
});

// 4. Cart Collection Indexes
db.cart.createIndex({ "userId": 1 }, { unique: true, sparse: true });
db.cart.createIndex({ "sessionId": 1 }, { unique: true, sparse: true });
db.cart.createIndex({ "expiresAt": 1 }, { expireAfterSeconds: 0 }); // TTL index
db.cart.createIndex({ "items.productId": 1 });
db.cart.createIndex({ "updatedAt": -1 });

// 5. Reviews Collection Indexes
db.reviews.createIndex({ "productId": 1, "status": 1, "createdAt": -1 });
db.reviews.createIndex({ "userId": 1, "createdAt": -1 });
db.reviews.createIndex({ "orderId": 1 });
db.reviews.createIndex({ "rating": -1, "helpfulVotes": -1 });
db.reviews.createIndex({ "status": 1, "createdAt": -1 });

// 6. Blog Posts Collection Indexes
db.blogPosts.createIndex({ "slug": 1 }, { unique: true });
db.blogPosts.createIndex({ "status": 1, "publishedAt": -1 });
db.blogPosts.createIndex({ "category": 1, "status": 1, "publishedAt": -1 });
db.blogPosts.createIndex({ "tags": 1, "status": 1 });
db.blogPosts.createIndex({ "authorId": 1, "status": 1 });
db.blogPosts.createIndex({ "featured": 1, "status": 1, "publishedAt": -1 });
// Full text search for blog
db.blogPosts.createIndex({ 
  "title": "text", 
  "content": "text", 
  "excerpt": "text",
  "tags": "text"
}, {
  weights: {
    title: 10,
    tags: 5,
    excerpt: 3,
    content: 1
  }
});

// 7. Analytics Collection Indexes
db.analytics.createIndex({ "type": 1, "timestamp": -1 });
db.analytics.createIndex({ "userId": 1, "type": 1, "timestamp": -1 });
db.analytics.createIndex({ "sessionId": 1, "timestamp": -1 });
db.analytics.createIndex({ "data.productId": 1, "type": 1, "timestamp": -1 });
db.analytics.createIndex({ "timestamp": -1 }, { expireAfterSeconds: 7776000 }); // 90 days TTL

// 8. Notifications Collection Indexes
db.notifications.createIndex({ "userId": 1, "createdAt": -1 });
db.notifications.createIndex({ "userId": 1, "status.inApp.read": 1, "createdAt": -1 });
db.notifications.createIndex({ "scheduledFor": 1, "status.email.sent": 1 });
db.notifications.createIndex({ "expiresAt": 1 }, { expireAfterSeconds: 0 });

// 9. Discount Codes Collection Indexes
db.discountCodes.createIndex({ "code": 1 }, { unique: true });
db.discountCodes.createIndex({ "isActive": 1, "startDate": 1, "endDate": 1 });
db.discountCodes.createIndex({ "applicableCategories": 1, "isActive": 1 });

// 10. Chat Messages Collection Indexes
db.chatMessages.createIndex({ "sessionId": 1, "createdAt": -1 });
db.chatMessages.createIndex({ "userId": 1, "createdAt": -1 });
db.chatMessages.createIndex({ "lastMessageAt": -1 });
db.chatMessages.createIndex({ "createdAt": 1 }, { expireAfterSeconds: 2592000 }); // 30 days TTL

// ==========================================
// AGGREGATION PIPELINE OPTIMIZATIONS
// ==========================================

// 1. Product Search with Filters Pipeline
function searchProducts(filters) {
  const pipeline = [
    // Stage 1: Match active products
    {
      $match: {
        "flags.isActive": true,
        ...(filters.category && { "category.main": filters.category }),
        ...(filters.priceMin && { "pricing.regular": { $gte: filters.priceMin } }),
        ...(filters.priceMax && { "pricing.regular": { $lte: filters.priceMax } }),
        ...(filters.tags && { tags: { $in: filters.tags } })
      }
    },
    
    // Stage 2: Lookup reviews for rating
    {
      $lookup: {
        from: "reviews",
        let: { productId: "$_id" },
        pipeline: [
          { $match: { 
            $expr: { $eq: ["$productId", "$$productId"] },
            status: "approved"
          }},
          { $group: {
            _id: null,
            avgRating: { $avg: "$rating" },
            count: { $sum: 1 }
          }}
        ],
        as: "reviewStats"
      }
    },
    
    // Stage 3: Add computed fields
    {
      $addFields: {
        "computedRating": { $arrayElemAt: ["$reviewStats.avgRating", 0] },
        "reviewCount": { $arrayElemAt: ["$reviewStats.count", 0] },
        "discountPercentage": {
          $cond: [
            { $and: [
              { $gt: ["$pricing.sale", 0] },
              { $lt: ["$pricing.sale", "$pricing.regular"] }
            ]},
            {
              $multiply: [
                { $divide: [
                  { $subtract: ["$pricing.regular", "$pricing.sale"] },
                  "$pricing.regular"
                ]},
                100
              ]
            },
            0
          ]
        }
      }
    },
    
    // Stage 4: Sort
    {
      $sort: filters.sortBy || { createdAt: -1 }
    },
    
    // Stage 5: Facet for pagination and filters
    {
      $facet: {
        metadata: [
          { $count: "total" },
          {
            $addFields: {
              page: filters.page || 1,
              limit: filters.limit || 20
            }
          }
        ],
        data: [
          { $skip: ((filters.page || 1) - 1) * (filters.limit || 20) },
          { $limit: filters.limit || 20 }
        ],
        availableFilters: [
          {
            $group: {
              _id: null,
              categories: { $addToSet: "$category.main" },
              tags: { $addToSet: "$tags" },
              priceRange: {
                $push: "$pricing.regular"
              }
            }
          },
          {
            $project: {
              categories: 1,
              tags: { $reduce: {
                input: "$tags",
                initialValue: [],
                in: { $setUnion: ["$$value", "$$this"] }
              }},
              minPrice: { $min: "$priceRange" },
              maxPrice: { $max: "$priceRange" }
            }
          }
        ]
      }
    }
  ];
  
  return db.products.aggregate(pipeline);
}

// 2. User Order History with Stats
function getUserOrderStats(userId) {
  return db.orders.aggregate([
    {
      $match: { 
        userId: ObjectId(userId),
        status: { $ne: "cancelled" }
      }
    },
    {
      $facet: {
        orderHistory: [
          { $sort: { createdAt: -1 } },
          { $limit: 10 },
          {
            $lookup: {
              from: "products",
              localField: "items.productId",
              foreignField: "_id",
              as: "productDetails"
            }
          }
        ],
        stats: [
          {
            $group: {
              _id: null,
              totalOrders: { $sum: 1 },
              totalSpent: { $sum: "$pricing.total" },
              avgOrderValue: { $avg: "$pricing.total" },
              favoriteCategories: { $push: "$items.productId" }
            }
          }
        ],
        monthlySpending: [
          {
            $group: {
              _id: {
                year: { $year: "$createdAt" },
                month: { $month: "$createdAt" }
              },
              total: { $sum: "$pricing.total" },
              orders: { $sum: 1 }
            }
          },
          { $sort: { "_id.year": -1, "_id.month": -1 } },
          { $limit: 12 }
        ]
      }
    }
  ]);
}

// ==========================================
// CACHING STRATEGIES
// ==========================================

// 1. Redis Cache Keys Structure
const cacheKeys = {
  // Product caching
  product: (id) => `product:${id}`,
  productList: (category, page) => `products:${category}:${page}`,
  featuredProducts: () => 'products:featured',
  
  // Category caching
  categories: () => 'categories:all',
  categoryTree: () => 'categories:tree',
  
  // User caching
  userCart: (userId) => `cart:user:${userId}`,
  userWishlist: (userId) => `wishlist:user:${userId}`,
  
  // Session caching
  session: (sessionId) => `session:${sessionId}`,
  
  // Analytics caching
  popularProducts: () => 'analytics:popular:products',
  realtimeStats: () => 'analytics:realtime',
  
  // Search caching
  searchResults: (query, filters) => `search:${query}:${JSON.stringify(filters)}`
};

// 2. Cache TTL Configuration
const cacheTTL = {
  product: 3600, // 1 hour
  productList: 900, // 15 minutes
  categories: 86400, // 24 hours
  userCart: 1800, // 30 minutes
  session: 7200, // 2 hours
  analytics: 300, // 5 minutes
  search: 600 // 10 minutes
};

// ==========================================
// DATABASE OPTIMIZATION SETTINGS
// ==========================================

// 1. Connection Pool Configuration
const mongoConfig = {
  maxPoolSize: 100,
  minPoolSize: 10,
  maxIdleTimeMS: 10000,
  waitQueueTimeoutMS: 5000,
  serverSelectionTimeoutMS: 5000,
  socketTimeoutMS: 45000,
  compressors: ['zstd', 'zlib'],
  retryWrites: true,
  retryReads: true,
  readPreference: 'secondaryPreferred',
  readConcern: { level: 'majority' },
  writeConcern: { 
    w: 'majority',
    j: true,
    wtimeout: 5000
  }
};

// 2. Sharding Strategy (for scale)
const shardingConfig = {
  users: {
    shardKey: { _id: "hashed" },
    numInitialChunks: 4
  },
  products: {
    shardKey: { "category.main": 1, _id: 1 },
    numInitialChunks: 8
  },
  orders: {
    shardKey: { userId: 1, _id: 1 },
    numInitialChunks: 8
  },
  analytics: {
    shardKey: { timestamp: 1, type: 1 },
    numInitialChunks: 12
  }
};

// 3. Collection Options
const collectionOptions = {
  products: {
    validator: {
      $jsonSchema: {
        bsonType: "object",
        required: ["name", "slug", "category", "pricing"],
        properties: {
          pricing: {
            bsonType: "object",
            required: ["regular", "currency"],
            properties: {
              regular: { bsonType: "number", minimum: 0 },
              sale: { bsonType: "number", minimum: 0 }
            }
          }
        }
      }
    },
    validationLevel: "moderate",
    validationAction: "warn"
  },
  
  orders: {
    validator: {
      $jsonSchema: {
        bsonType: "object",
        required: ["orderNumber", "userId", "items", "pricing", "status"],
        properties: {
          status: {
            enum: ["pending", "confirmed", "processing", "shipping", "delivered", "cancelled"]
          }
        }
      }
    },
    validationLevel: "strict",
    validationAction: "error"
  }
};
