package com.newsapp.newsapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.newsapp.rssproducer.model.ArticleDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        if (redisHost == null || redisHost.isEmpty()) {
            log.error("Redis host is not configured");
            throw new IllegalArgumentException("Redis host must be set");
        }
        if (redisPort <= 0) {
            log.error("Invalid Redis port: {}", redisPort);
            throw new IllegalArgumentException("Redis port must be a positive integer");
        }
        try {
            RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
            return new LettuceConnectionFactory(config);
        } catch (Exception e) {
            log.error("Failed to create RedisConnectionFactory", e);
            throw e;
        }
    }

    @Bean
    public RedisTemplate<String, ArticleDto> redisTemplate() {
        try {
            RedisTemplate<String, ArticleDto> template = new RedisTemplate<>();
            template.setConnectionFactory(redisConnectionFactory());
            template.setKeySerializer(new StringRedisSerializer());

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            Jackson2JsonRedisSerializer<ArticleDto> serializer = new Jackson2JsonRedisSerializer<>(mapper, ArticleDto.class);
            template.setValueSerializer(serializer);
            template.setHashKeySerializer(new StringRedisSerializer());
            template.setHashValueSerializer(serializer);

            return template;
        } catch (Exception e) {
            log.error("Failed to configure RedisTemplate", e);
            throw e;
        }
    }
}