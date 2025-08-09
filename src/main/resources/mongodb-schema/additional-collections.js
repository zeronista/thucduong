// Additional Collections for E-commerce Platform

// 1. Payments Collection - Tách riêng để quản lý thanh toán
{
  _id: ObjectId,
  orderId: ObjectId,
  userId: ObjectId,
  amount: {
    value: Number,
    currency: String
  },
  method: String, // 'COD', 'bank_transfer', 'momo', 'vnpay', 'zalopay'
  provider: {
    name: String,
    transactionId: String,
    responseCode: String,
    responseMessage: String
  },
  status: String, // 'pending', 'processing', 'completed', 'failed', 'refunded'
  statusHistory: [{
    status: String,
    timestamp: Date,
    reason: String
  }],
  metadata: Object, // Provider-specific data
  ipAddress: String,
  userAgent: String,
  createdAt: Date,
  updatedAt: Date,
  completedAt: Date,
  refunds: [{
    amount: Number,
    reason: String,
    refundedAt: Date,
    refundId: String
  }]
}

// 2. Notifications Collection
{
  _id: ObjectId,
  userId: ObjectId,
  type: String, // 'order', 'promotion', 'system', 'blog'
  title: String,
  message: String,
  data: Object, // Related data (orderId, productId, etc.)
  channels: [String], // 'email', 'sms', 'push', 'in-app'
  status: {
    email: { sent: Boolean, sentAt: Date, error: String },
    sms: { sent: Boolean, sentAt: Date, error: String },
    push: { sent: Boolean, sentAt: Date, error: String },
    inApp: { read: Boolean, readAt: Date }
  },
  priority: String, // 'high', 'medium', 'low'
  scheduledFor: Date,
  expiresAt: Date,
  createdAt: Date,
  updatedAt: Date
}

// 3. Shipping Methods Collection
{
  _id: ObjectId,
  name: String,
  code: String, // 'standard', 'express', 'ghn', 'ghtk'
  provider: {
    name: String,
    apiEndpoint: String,
    apiKey: String // encrypted
  },
  description: String,
  estimatedDays: {
    min: Number,
    max: Number
  },
  pricing: {
    type: String, // 'fixed', 'weight_based', 'distance_based'
    basePrice: Number,
    rules: [{
      condition: Object, // { weight: { min: 0, max: 1 } }
      price: Number
    }]
  },
  restrictions: {
    minOrderValue: Number,
    maxWeight: Number,
    availableCities: [String],
    excludedProducts: [ObjectId]
  },
  trackingUrl: String,
  isActive: Boolean,
  displayOrder: Number,
  createdAt: Date,
  updatedAt: Date
}

// 4. Tax Settings Collection
{
  _id: ObjectId,
  name: String, // 'VAT', 'Import Tax'
  code: String,
  rate: Number, // percentage
  applicableCategories: [String],
  excludedCategories: [String],
  isInclusive: Boolean, // tax included in price
  isActive: Boolean,
  validFrom: Date,
  validTo: Date,
  createdAt: Date,
  updatedAt: Date
}

// 5. Product Variants Collection (for size/packaging options)
{
  _id: ObjectId,
  productId: ObjectId,
  sku: String,
  name: String, // '500g', '1kg', 'Combo 3 gói'
  attributes: {
    weight: Number,
    size: String,
    packaging: String
  },
  pricing: {
    regular: Number,
    sale: Number
  },
  stock: {
    quantity: Number,
    reserved: Number // for pending orders
  },
  images: [String],
  barcode: String,
  isDefault: Boolean,
  isActive: Boolean,
  createdAt: Date,
  updatedAt: Date
}

// 6. Wishlists Collection (tách riêng từ Users để tối ưu)
{
  _id: ObjectId,
  userId: ObjectId,
  items: [{
    productId: ObjectId,
    variantId: ObjectId,
    addedAt: Date,
    notes: String,
    priceAlert: {
      enabled: Boolean,
      targetPrice: Number
    }
  }],
  isPublic: Boolean,
  sharedWith: [String], // email addresses
  createdAt: Date,
  updatedAt: Date
}

// 7. SEO Metadata Collection
{
  _id: ObjectId,
  path: String, // URL path
  type: String, // 'product', 'category', 'blog', 'page'
  referenceId: ObjectId, // ID of related document
  title: String,
  description: String,
  keywords: [String],
  ogTags: {
    title: String,
    description: String,
    image: String,
    type: String
  },
  structuredData: Object, // JSON-LD
  canonicalUrl: String,
  robots: String, // 'index,follow'
  customMeta: [{
    name: String,
    content: String
  }],
  createdAt: Date,
  updatedAt: Date
}

// 8. Customer Support Tickets
{
  _id: ObjectId,
  ticketNumber: String,
  userId: ObjectId,
  orderId: ObjectId, // optional
  subject: String,
  category: String, // 'order', 'product', 'shipping', 'payment', 'other'
  priority: String, // 'low', 'medium', 'high', 'urgent'
  status: String, // 'open', 'in_progress', 'waiting_customer', 'resolved', 'closed'
  messages: [{
    senderId: ObjectId,
    senderType: String, // 'customer', 'support', 'system'
    message: String,
    attachments: [String],
    timestamp: Date
  }],
  assignedTo: ObjectId, // support staff
  tags: [String],
  resolution: String,
  satisfactionRating: Number,
  createdAt: Date,
  updatedAt: Date,
  resolvedAt: Date
}
