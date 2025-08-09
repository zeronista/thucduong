// Improvements for Existing Collections

// 1. Enhanced Users Collection
{
  _id: ObjectId,
  email: String,
  password: String, // hashed with bcrypt
  fullName: String,
  phoneNumber: String,
  role: String, // 'customer', 'admin', 'author', 'support'
  
  // Profile enhancements
  profile: {
    avatar: String,
    dateOfBirth: Date,
    gender: String,
    healthConditions: [String], // for personalized recommendations
    dietaryPreferences: [String], // 'vegetarian', 'vegan', 'gluten-free'
    allergies: [String]
  },
  
  // Address improvements
  addresses: [{
    _id: ObjectId,
    type: String, // 'home', 'work', 'other'
    recipientName: String,
    recipientPhone: String,
    street: String,
    ward: String,
    district: String,
    city: String,
    province: String,
    postalCode: String,
    landmark: String,
    deliveryInstructions: String,
    coordinates: {
      lat: Number,
      lng: Number
    },
    isDefault: Boolean,
    isVerified: Boolean
  }],
  
  // Loyalty program
  loyalty: {
    points: Number,
    tier: String, // 'bronze', 'silver', 'gold', 'platinum'
    tierExpiryDate: Date,
    lifetimeValue: Number,
    referralCode: String,
    referredBy: ObjectId
  },
  
  // Preferences
  preferences: {
    newsletter: Boolean,
    smsNotifications: Boolean,
    pushNotifications: Boolean,
    language: String, // 'vi', 'en'
    currency: String,
    timezone: String
  },
  
  // Security enhancements
  security: {
    twoFactorEnabled: Boolean,
    twoFactorSecret: String,
    loginAttempts: Number,
    lockedUntil: Date,
    passwordHistory: [String], // hashed previous passwords
    lastPasswordChange: Date,
    securityQuestions: [{
      question: String,
      answer: String // hashed
    }]
  },
  
  // Activity tracking
  activity: {
    lastLogin: Date,
    lastPurchase: Date,
    totalOrders: Number,
    totalSpent: Number,
    averageOrderValue: Number,
    lastActivityAt: Date
  },
  
  // Consent management (GDPR)
  consent: {
    marketing: { given: Boolean, timestamp: Date },
    dataProcessing: { given: Boolean, timestamp: Date },
    cookies: { given: Boolean, timestamp: Date }
  },
  
  wishlist: [ObjectId], // references to Product._id
  compareList: [ObjectId], // for product comparison
  recentlyViewed: [{
    productId: ObjectId,
    viewedAt: Date
  }],
  
  createdAt: Date,
  updatedAt: Date,
  deletedAt: Date, // soft delete
  isActive: Boolean,
  emailVerified: Boolean,
  phoneVerified: Boolean,
  resetPasswordToken: String,
  resetPasswordExpires: Date
}

// 2. Enhanced Products Collection
{
  _id: ObjectId,
  name: String,
  slug: String,
  sku: String, // Stock Keeping Unit
  
  category: {
    main: String,
    sub: String,
    tags: [String] // for better categorization
  },
  
  // Pricing improvements
  pricing: {
    cost: Number, // for profit calculation
    regular: Number,
    sale: Number,
    bulkPricing: [{
      minQuantity: Number,
      price: Number
    }],
    currency: String,
    taxable: Boolean,
    taxClass: String
  },
  
  // Enhanced media
  media: {
    images: [{
      url: String,
      alt: String,
      caption: String,
      isPrimary: Boolean,
      order: Number,
      dimensions: {
        width: Number,
        height: Number
      }
    }],
    videos: [{
      url: String,
      thumbnail: String,
      duration: Number,
      type: String // 'product_demo', 'recipe', 'testimonial'
    }],
    documents: [{
      name: String,
      url: String,
      type: String // 'certificate', 'lab_report', 'user_manual'
    }]
  },
  
  description: {
    short: String,
    detailed: String,
    usage: String, // how to use
    storage: String, // storage instructions
    warnings: String
  },
  
  // Detailed ingredients with benefits
  ingredients: [{
    name: String,
    percentage: Number,
    benefits: [String],
    isOrganic: Boolean,
    allergen: Boolean
  }],
  
  // Enhanced nutritional info per serving
  nutritionalInfo: {
    servingSize: String,
    servingsPerPackage: Number,
    calories: Number,
    caloriesFromFat: Number,
    nutrients: {
      protein: { amount: Number, unit: String, dailyValue: Number },
      carbs: { amount: Number, unit: String, dailyValue: Number },
      fat: { amount: Number, unit: String, dailyValue: Number },
      saturatedFat: { amount: Number, unit: String, dailyValue: Number },
      transFat: { amount: Number, unit: String, dailyValue: Number },
      cholesterol: { amount: Number, unit: String, dailyValue: Number },
      sodium: { amount: Number, unit: String, dailyValue: Number },
      fiber: { amount: Number, unit: String, dailyValue: Number },
      sugar: { amount: Number, unit: String, dailyValue: Number },
      addedSugar: { amount: Number, unit: String, dailyValue: Number }
    },
    vitamins: [{
      name: String,
      amount: Number,
      unit: String,
      dailyValue: Number
    }],
    minerals: [{
      name: String,
      amount: Number,
      unit: String,
      dailyValue: Number
    }]
  },
  
  // Enhanced health benefits with scientific backing
  healthBenefits: [{
    bodyPart: String,
    benefit: String,
    description: String,
    scientificEvidence: String,
    studies: [{
      title: String,
      url: String,
      year: Number
    }]
  }],
  
  // Certifications
  certifications: [{
    name: String, // 'Organic', 'Non-GMO', 'Fair Trade'
    certifier: String,
    certificateNumber: String,
    validUntil: Date,
    documentUrl: String
  }],
  
  // Enhanced inventory
  inventory: {
    quantity: Number,
    reserved: Number, // for pending orders
    unit: String,
    lowStockThreshold: Number,
    outOfStockThreshold: Number,
    maxOrderQuantity: Number,
    minOrderQuantity: Number,
    trackInventory: Boolean,
    allowBackorder: Boolean,
    locations: [{
      warehouse: String,
      quantity: Number
    }]
  },
  
  // Manufacturing details
  manufacturer: {
    name: String,
    code: String,
    origin: {
      country: String,
      region: String
    },
    certifications: [String]
  },
  
  // Shipping info
  shipping: {
    weight: Number,
    weightUnit: String,
    dimensions: {
      length: Number,
      width: Number,
      height: Number,
      unit: String
    },
    shippingClass: String,
    freeShipping: Boolean,
    additionalShippingCost: Number
  },
  
  // Enhanced ratings with breakdown
  ratings: {
    average: Number,
    count: Number,
    distribution: {
      5: Number,
      4: Number,
      3: Number,
      2: Number,
      1: Number
    },
    aspects: {
      quality: Number,
      value: Number,
      effectiveness: Number,
      taste: Number
    }
  },
  
  // Related products
  related: {
    crossSells: [ObjectId], // buy together
    upSells: [ObjectId], // premium alternatives
    similar: [ObjectId],
    frequently_bought_together: [{
      products: [ObjectId],
      discount: Number
    }]
  },
  
  // Search optimization
  searchKeywords: [String],
  synonyms: [String],
  
  tags: [String],
  badges: [String], // 'new', 'bestseller', 'limited', 'seasonal'
  
  // Flags
  flags: {
    isActive: Boolean,
    isFeatured: Boolean,
    isNew: Boolean,
    isBestseller: Boolean,
    isExclusive: Boolean,
    requiresPrescription: Boolean
  },
  
  // Analytics
  analytics: {
    views: Number,
    uniqueViews: Number,
    addedToCart: Number,
    purchased: Number,
    conversionRate: Number
  },
  
  // Dates
  publishedAt: Date,
  availableFrom: Date,
  availableUntil: Date,
  createdAt: Date,
  updatedAt: Date,
  deletedAt: Date, // soft delete
  
  // SEO with multi-language support
  seo: {
    vi: {
      metaTitle: String,
      metaDescription: String,
      keywords: [String]
    },
    en: {
      metaTitle: String,
      metaDescription: String,
      keywords: [String]
    }
  }
}

// 3. Enhanced Body Health Map Collection
{
  _id: ObjectId,
  bodyPart: String,
  code: String, // unique identifier
  
  // Multi-language display
  displayName: {
    vi: String,
    en: String
  },
  
  // Enhanced visualization
  visualization: {
    svgPath: String,
    imageMapCoords: String,
    position: {
      x: Number,
      y: Number
    },
    zIndex: Number, // for layering
    animations: {
      hover: String, // CSS class or animation name
      click: String
    },
    hotspots: [{ // clickable areas
      x: Number,
      y: Number,
      radius: Number,
      label: String
    }]
  },
  
  // Detailed health information
  healthInfo: {
    primaryFunctions: [{
      title: { vi: String, en: String },
      description: { vi: String, en: String },
      importance: String // 'critical', 'major', 'minor'
    }],
    
    commonIssues: [{
      name: { vi: String, en: String },
      symptoms: [String],
      causes: [String],
      prevention: [String],
      relatedProducts: [ObjectId]
    }],
    
    nutritionalNeeds: [{
      nutrient: String,
      dailyRequirement: String,
      benefits: [String],
      sources: [String],
      deficiencySymptoms: [String]
    }],
    
    macrobioticPrinciples: {
      yinYangBalance: String,
      seasonalConsiderations: Object,
      cookingMethods: [String],
      avoidFoods: [String]
    }
  },
  
  // Interactive elements
  interactions: {
    didYouKnow: [{
      fact: { vi: String, en: String },
      source: String,
      relatedProducts: [ObjectId]
    }],
    
    tips: [{
      title: { vi: String, en: String },
      content: { vi: String, en: String },
      category: String // 'nutrition', 'lifestyle', 'exercise'
    }],
    
    recipes: [{
      name: String,
      benefits: [String],
      ingredients: [String],
      blogPostId: ObjectId
    }]
  },
  
  // Product recommendations with rules
  recommendations: {
    products: [{
      productId: ObjectId,
      reason: String,
      priority: Number,
      conditions: Object // e.g., { age: { min: 30 }, gender: 'female' }
    }],
    
    bundles: [{
      name: String,
      products: [ObjectId],
      discount: Number,
      description: String
    }]
  },
  
  // Metadata
  metadata: {
    medicalDisclaimer: String,
    lastReviewedBy: String,
    lastReviewedDate: Date,
    references: [{
      title: String,
      url: String,
      type: String // 'research', 'article', 'book'
    }]
  },
  
  displayOrder: Number,
  isActive: Boolean,
  createdAt: Date,
  updatedAt: Date
}
