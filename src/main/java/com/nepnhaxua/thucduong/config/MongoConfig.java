package com.nepnhaxua.thucduong.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.validation.Validator;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableMongoRepositories(basePackages = "com.nepnhaxua.thucduong.repository")
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Value("${spring.data.mongodb.connection-pool.max-size:100}")
    private int maxPoolSize;

    @Value("${spring.data.mongodb.connection-pool.min-size:10}")
    private int minPoolSize;

    @Value("${spring.data.mongodb.connection-pool.max-wait-time:5000}")
    private int maxWaitTime;

    @Value("${spring.data.mongodb.connection-pool.max-connection-idle-time:10000}")
    private int maxConnectionIdleTime;

    @NotNull
    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @NotNull
    @Override
    @Bean
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(mongoUri);

        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToConnectionPoolSettings(settings -> settings
                        .maxSize(maxPoolSize)
                        .minSize(minPoolSize)
                        .maxWaitTime(maxWaitTime, TimeUnit.MILLISECONDS)
                        .maxConnectionIdleTime(maxConnectionIdleTime, TimeUnit.MILLISECONDS))
                .applyToSocketSettings(settings -> settings
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .readTimeout(5, TimeUnit.SECONDS))
                .applyToServerSettings(settings -> settings
                        .heartbeatFrequency(10, TimeUnit.SECONDS)
                        .minHeartbeatFrequency(500, TimeUnit.MILLISECONDS))
                .retryWrites(true)
                .retryReads(true)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @NotNull
    @Bean
    public MongoTemplate mongoTemplate(@NotNull MongoDatabaseFactory mongoDbFactory,
            @NotNull MappingMongoConverter converter) {
        return new MongoTemplate(mongoDbFactory, converter);
    }

    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory factory,
            MongoMappingContext context,
            MongoCustomConversions conversions) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
        mappingConverter.setCustomConversions(conversions);

        // Don't save _class to mongo documents
        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));

        return mappingConverter;
    }

    @Bean
    public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener(Validator validator) {
        return new ValidatingMongoEventListener(validator);
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    // Custom conversions can be added here
    @NotNull
    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(java.util.Collections.emptyList());
    }

    // Auditing configuration
    @Bean
    public MongoAuditingConfig mongoAuditingConfig() {
        return new MongoAuditingConfig();
    }
}
