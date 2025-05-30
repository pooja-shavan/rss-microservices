package com.newsconsumer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.newsapp.rssproducer.model.ArticleDto;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);

    private final RedisTemplate<String, ArticleDto> redisTemplate;
    private final int ttlHours;

    public RedisService(
            RedisTemplate<String, ArticleDto> redisTemplate,
            @Value("${redis.ttl.hours}") int ttlHours) {
        this.redisTemplate = redisTemplate;
        this.ttlHours = ttlHours;
    }

    /**
     * Check if article with given ID exists in Redis
     */
    public boolean exists(String id) {
        Boolean exists = redisTemplate.hasKey(id);
        return exists != null && exists;
    }

    /**
     * Save article to Redis with TTL
     */
    public void saveArticle(ArticleDto article) {
        logger.info("Saving article to Redis: {}", article);
        redisTemplate.opsForValue().set(article.getId(), article, ttlHours, TimeUnit.HOURS);
    }

    /**
     * Get article by ID
     */
    public ArticleDto getArticle(String id) {
        return redisTemplate.opsForValue().get(id);
    }
}