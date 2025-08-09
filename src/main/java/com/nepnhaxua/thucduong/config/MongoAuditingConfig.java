package com.nepnhaxua.thucduong.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
public class MongoAuditingConfig {
    // This class enables MongoDB auditing for @CreatedDate and @LastModifiedDate annotations
}
