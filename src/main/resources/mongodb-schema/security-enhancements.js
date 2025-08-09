// Security Enhancements for MongoDB Schema

// ==========================================
// DATA ENCRYPTION & PROTECTION
// ==========================================

// 1. Field-Level Encryption Configuration
const encryptedFields = {
  users: {
    // Client-Side Field Level Encryption (CSFLE)
    encryptedFields: {
      fields: [
        {
          path: "email",
          bsonType: "string",
          queries: { queryType: "equality" }
        },
        {
          path: "phoneNumber", 
          bsonType: "string",
          queries: { queryType: "equality" }
        },
        {
          path: "addresses.street",
          bsonType: "string"
        },
        {
          path: "security.securityQuestions.answer",
          bsonType: "string"
        }
      ]
    }
  },
  
  payments: {
    encryptedFields: {
      fields: [
        {
          path: "provider.transactionId",
          bsonType: "string"
        },
        {
          path: "metadata",
          bsonType: "object"
        }
      ]
    }
  }
};

// 2. Audit Collection for Security Events
{
  _id: ObjectId,
  eventType: String, // 'login', 'logout', 'password_change', 'data_access', 'admin_action'
  severity: String, // 'info', 'warning', 'critical'
  userId: ObjectId,
  userEmail: String, // denormalized for quick access
  userRole: String,
  
  details: {
    action: String,
    resource: String, // collection/document accessed
    resourceId: ObjectId,
    changes: Object, // for update operations
    reason: String
  },
  
  security: {
    ipAddress: String,
    userAgent: String,
    location: {
      country: String,
      city: String,
      coordinates: {
        lat: Number,
        lng: Number
      }
    },
    deviceFingerprint: String,
    sessionId: String
  },
  
  flags: {
    suspicious: Boolean,
    blocked: Boolean,
    requiresReview: Boolean
  },
  
  timestamp: Date,
  expiresAt: Date // for automatic cleanup per data retention policy
}

// 3. API Keys Collection for Service Authentication
{
  _id: ObjectId,
  name: String,
  key: String, // hashed
  prefix: String, // first 8 chars for identification
  type: String, // 'public', 'private', 'webhook'
  
  permissions: {
    scopes: [String], // 'read:products', 'write:orders', etc.
    ipWhitelist: [String],
    origins: [String], // for CORS
    rateLimit: {
      requests: Number,
      window: Number // seconds
    }
  },
  
  usage: {
    lastUsed: Date,
    totalRequests: Number,
    monthlyRequests: [{
      month: Date,
      count: Number
    }]
  },
  
  metadata: {
    createdBy: ObjectId,
    description: String,
    environment: String // 'development', 'staging', 'production'
  },
  
  isActive: Boolean,
  expiresAt: Date,
  createdAt: Date,
  updatedAt: Date
}

// ==========================================
// SECURITY POLICIES & RULES
// ==========================================

// 1. Password Policy Configuration
const passwordPolicy = {
  minLength: 8,
  maxLength: 128,
  requireUppercase: true,
  requireLowercase: true,
  requireNumbers: true,
  requireSpecialChars: true,
  prohibitedPasswords: [
    // Common passwords list
    "password123", "123456789", "qwerty123"
  ],
  preventReuse: 5, // last 5 passwords
  expiryDays: 90,
  warningDays: 7
};

// 2. Session Security Configuration
const sessionConfig = {
  timeout: {
    idle: 1800, // 30 minutes
    absolute: 28800, // 8 hours
    remember: 2592000 // 30 days
  },
  concurrent: {
    maxSessions: 3,
    action: 'terminate_oldest' // or 'prevent_new'
  },
  tracking: {
    logIpChanges: true,
    logDeviceChanges: true,
    requireReauth: ['payment', 'profile_update', 'password_change']
  }
};

// 3. Rate Limiting Rules
const rateLimits = {
  global: {
    windowMs: 900000, // 15 minutes
    max: 1000
  },
  
  endpoints: {
    '/api/auth/login': {
      windowMs: 900000,
      max: 5,
      skipSuccessfulRequests: true
    },
    '/api/auth/register': {
      windowMs: 3600000, // 1 hour
      max: 3
    },
    '/api/auth/forgot-password': {
      windowMs: 3600000,
      max: 3
    },
    '/api/products/search': {
      windowMs: 60000, // 1 minute
      max: 30
    },
    '/api/orders': {
      windowMs: 3600000,
      max: 10
    }
  }
};

// ==========================================
// MONGODB SECURITY CONFIGURATION
// ==========================================

// 1. Role-Based Access Control (RBAC)
const mongoRoles = [
  {
    role: "customer_user",
    privileges: [
      {
        resource: { db: "ecommerce", collection: "users" },
        actions: ["find", "update"],
        // Only own document
        filter: { _id: "$$USER_ID" }
      },
      {
        resource: { db: "ecommerce", collection: "products" },
        actions: ["find"]
      },
      {
        resource: { db: "ecommerce", collection: "orders" },
        actions: ["find", "insert"],
        filter: { userId: "$$USER_ID" }
      },
      {
        resource: { db: "ecommerce", collection: "cart" },
        actions: ["find", "insert", "update", "remove"],
        filter: { userId: "$$USER_ID" }
      },
      {
        resource: { db: "ecommerce", collection: "reviews" },
        actions: ["find", "insert"],
        filter: { userId: "$$USER_ID" }
      }
    ]
  },
  
  {
    role: "admin_user",
    privileges: [
      {
        resource: { db: "ecommerce", collection: "" },
        actions: ["find", "insert", "update", "remove", "createIndex", "dropIndex"]
      }
    ],
    roles: ["dbAdmin", "userAdmin"]
  },
  
  {
    role: "support_user",
    privileges: [
      {
        resource: { db: "ecommerce", collection: "users" },
        actions: ["find"]
      },
      {
        resource: { db: "ecommerce", collection: "orders" },
        actions: ["find", "update"]
      },
      {
        resource: { db: "ecommerce", collection: "tickets" },
        actions: ["find", "insert", "update"]
      }
    ]
  },
  
  {
    role: "analytics_user",
    privileges: [
      {
        resource: { db: "ecommerce", collection: "" },
        actions: ["find"],
        // Exclude sensitive collections
        filter: { 
          collection: { 
            $nin: ["payments", "audit", "apiKeys"] 
          }
        }
      }
    ]
  }
];

// 2. Network Security Configuration
const networkSecurity = {
  bindIp: ["127.0.0.1", "10.0.0.0/8"], // Restrict to local and private network
  port: 27017,
  ssl: {
    mode: "requireSSL",
    PEMKeyFile: "/path/to/mongodb.pem",
    CAFile: "/path/to/ca.pem",
    allowConnectionsWithoutCertificates: false
  },
  authentication: {
    mechanism: "SCRAM-SHA-256",
    authenticationDatabase: "admin"
  }
};

// 3. Audit Configuration
const auditConfig = {
  destination: "file",
  format: "JSON",
  path: "/var/log/mongodb/audit.json",
  filter: {
    atype: { $in: ["authenticate", "createCollection", "dropCollection", 
                   "createDatabase", "dropDatabase", "createUser", 
                   "dropUser", "updateUser", "grantRole", "revokeRole"] }
  }
};

// ==========================================
// SECURITY VALIDATION FUNCTIONS
// ==========================================

// 1. Input Validation Schemas
const validationSchemas = {
  userRegistration: {
    email: {
      type: "string",
      pattern: "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
      maxLength: 254
    },
    password: {
      type: "string",
      minLength: 8,
      maxLength: 128,
      pattern: "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]"
    },
    fullName: {
      type: "string",
      pattern: "^[a-zA-ZÀ-ỹĂăÂâĐđÊêÔôƠơƯư\\s'-]{2,50}$",
      maxLength: 50
    },
    phoneNumber: {
      type: "string",
      pattern: "^(\\+84|0)(3|5|7|8|9)[0-9]{8}$"
    }
  },
  
  productSearch: {
    query: {
      type: "string",
      maxLength: 100,
      sanitize: true // Remove special chars
    },
    category: {
      type: "string",
      enum: ["gạo lứt", "bún lứt", "trà thảo mộc", "thực phẩm khác"]
    },
    priceMin: {
      type: "number",
      minimum: 0,
      maximum: 100000000
    },
    priceMax: {
      type: "number",
      minimum: 0,
      maximum: 100000000
    }
  }
};

// 2. Sanitization Functions
const sanitizers = {
  // Remove potential NoSQL injection attempts
  sanitizeQuery: (query) => {
    if (typeof query !== 'object') return query;
    
    const cleaned = {};
    for (const key in query) {
      if (key.startsWith('$')) continue; // Remove operators
      
      const value = query[key];
      if (typeof value === 'object') {
        cleaned[key] = sanitizers.sanitizeQuery(value);
      } else {
        cleaned[key] = value;
      }
    }
    return cleaned;
  },
  
  // Sanitize HTML content
  sanitizeHtml: (html) => {
    // Use DOMPurify or similar library
    const allowedTags = ['p', 'br', 'strong', 'em', 'u', 'h1', 'h2', 'h3', 
                        'ul', 'ol', 'li', 'blockquote', 'a', 'img'];
    const allowedAttributes = {
      'a': ['href', 'title'],
      'img': ['src', 'alt', 'width', 'height']
    };
    // Implementation would use a library
    return html;
  }
};

// ==========================================
// SECURITY MONITORING
// ==========================================

// 1. Suspicious Activity Detection Rules
const suspiciousActivityRules = [
  {
    name: "multiple_failed_logins",
    condition: {
      event: "login_failed",
      count: 5,
      timeWindow: 900 // 15 minutes
    },
    action: "block_ip",
    severity: "high"
  },
  {
    name: "unusual_location",
    condition: {
      event: "login_success",
      locationChange: true,
      distance: 1000 // km
    },
    action: "require_2fa",
    severity: "medium"
  },
  {
    name: "bulk_data_access",
    condition: {
      event: "data_export",
      recordCount: 1000,
      timeWindow: 3600
    },
    action: "alert_admin",
    severity: "high"
  },
  {
    name: "privilege_escalation_attempt",
    condition: {
      event: "unauthorized_access",
      targetRole: ["admin", "support"]
    },
    action: "block_user",
    severity: "critical"
  }
];

// 2. Security Headers Configuration
const securityHeaders = {
  "Strict-Transport-Security": "max-age=31536000; includeSubDomains",
  "X-Content-Type-Options": "nosniff",
  "X-Frame-Options": "DENY",
  "X-XSS-Protection": "1; mode=block",
  "Content-Security-Policy": "default-src 'self'; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://apis.google.com; style-src 'self' 'unsafe-inline';",
  "Referrer-Policy": "strict-origin-when-cross-origin",
  "Permissions-Policy": "geolocation=(), microphone=(), camera=()"
};

// ==========================================
// GDPR & DATA PRIVACY
// ==========================================

// 1. Personal Data Tracking
const personalDataFields = {
  users: ["email", "fullName", "phoneNumber", "addresses", "profile"],
  orders: ["shippingAddress", "payment.transactionId"],
  reviews: ["comment", "images"],
  chatMessages: ["messages.content"],
  newsletters: ["email", "name"]
};

// 2. Data Retention Policies
const dataRetention = {
  users: {
    active: null, // Keep indefinitely
    inactive: 730, // 2 years
    deleted: 30 // 30 days before permanent deletion
  },
  orders: {
    completed: 2555, // 7 years for tax purposes
    cancelled: 365
  },
  analytics: {
    pageViews: 90,
    userSessions: 180,
    aggregated: 730
  },
  audit: {
    security: 365,
    admin: 730,
    user: 180
  },
  chatMessages: 30,
  cart: 7
};

// 3. Data Export Schema (for GDPR requests)
const dataExportSchema = {
  format: "json",
  includeCollections: [
    "users", "orders", "reviews", "addresses", 
    "wishlist", "cart", "chatMessages"
  ],
  excludeFields: [
    "password", "resetPasswordToken", "security.twoFactorSecret",
    "security.passwordHistory", "payment.provider.apiKey"
  ],
  anonymizeFields: [
    "ipAddress", "deviceFingerprint", "sessionId"
  ]
};
