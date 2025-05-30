package com.newsconsumer.service;

import com.newsapp.rssproducer.model.ArticleDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;

@Service
public class KafkaConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    private final RedisService redisService;

    public KafkaConsumerService(RedisService redisService) {
        this.redisService = redisService;
    }

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ArticleDto articleDto) {
        logger.info("Received RSS item from Kafka: {}", articleDto);

        // Check if publishedAt is within the last 24 hours
        if (isPublishedWithinLast24Hours(articleDto)) {
            logger.info("Article is within 24-hour timeframe");

            // Check if article already exists in Redis
            if (redisService.exists(articleDto.getId())) {
                logger.info("Article already exists in Redis, skipping: {}", articleDto.getId());
            } else {
                logger.info("Saving new article to Redis: {}", articleDto.getId());
                redisService.saveArticle(articleDto);
            }
        } else {
            logger.info("Article is older than 24 hours, skipping: {}", articleDto.getId());
        }
    }

    private boolean isPublishedWithinLast24Hours(ArticleDto articleDto) {
        if (articleDto.getPublishedAt() == null) {
            logger.warn("RSS item has no publication date, treating as current: {}", articleDto.getId());
            return true;
        }

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime twentyFourHoursAgo = now.minusHours(24);

        return !articleDto.getPublishedAt().isBefore(ChronoLocalDateTime.from(twentyFourHoursAgo));
    }
}