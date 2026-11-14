package org.example.flowmanager.config;

import org.example.flowmanager.model.dto.UserSubscription;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;

@Configuration
public class RedisCacheConfig {

    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory connectionFactory,
            JsonMapper jsonMapper
    ) {
        var serializer = new JacksonJsonRedisSerializer<>(
                jsonMapper,
                UserSubscription.class
        );

        var subscriptionCache = RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))
                .disableCachingNullValues()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(serializer)
                );

        return RedisCacheManager.builder(connectionFactory)
                .withCacheConfiguration("subscriptions", subscriptionCache)
                .build();
    }
}