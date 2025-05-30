package com.newsapp.newsapi.service;

import com.newsapp.newsapi.model.Language;


import com.newsapp.newsapi.model.NewsResponse;
import com.newsapp.rssproducer.model.ArticleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NewsService {

    private final RedisTemplate<String, ArticleDto> redisTemplate;

    @Value("${redis.article.key-pattern}")
    private String articleKeyPattern;

    @Autowired
    public NewsService(RedisTemplate<String, ArticleDto> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Cacheable(value = "newsCache", key = "{#page, #pageSize, #sortBy, #sortDir, #language}")
    public NewsResponse getNews(int page, int pageSize, String sortBy, String sortDir, Language language) {
        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 10;

        // Get all articles from Redis
        List<ArticleDto> allArticles = getAllArticlesFromRedis();

        // Sort articles
        List<ArticleDto> sortedArticles = sortArticles(allArticles, sortBy, sortDir);

        // Paginate
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, sortedArticles.size());

        List<ArticleDto> paginatedArticles;
        if (start >= sortedArticles.size()) {
            paginatedArticles = new ArrayList<>();
        } else {
            paginatedArticles = new ArrayList<>(sortedArticles.subList(start, end));
        }

        return NewsResponse.builder()
                .articles(allArticles)
                .totalResults(allArticles.size())
                .page(page)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .language(language)
                .build();
    }

    private List<ArticleDto> getAllArticlesFromRedis() {
        List<ArticleDto> articles = new ArrayList<>();

        // Get all keys matching the pattern
        Set<String> keys = redisTemplate.keys(articleKeyPattern);
        if (keys != null && !keys.isEmpty()) {
            // Get all values
            List<ArticleDto> values = redisTemplate.opsForValue().multiGet(keys);
            if (values != null) {
                values.forEach(value -> {
                    if (value instanceof ArticleDto) {
                        articles.add((ArticleDto) value);
                    }
                });
            }
        }

        return articles;
    }

    private List<ArticleDto> sortArticles(List<ArticleDto> articles, String sortBy, String sortDir) {
        Comparator<ArticleDto> comparator;

        switch (sortBy.toLowerCase()) {
            case "title":
                comparator = Comparator.comparing(ArticleDto::getTitle,
                        Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "author":
                comparator = Comparator.comparing(ArticleDto::getAuthor,
                        Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "publishedat":
            default:
                comparator = Comparator.comparing(ArticleDto::getPublishedAt,
                        Comparator.nullsLast(Comparator.naturalOrder()));
                break;
        }

        if (sortDir.equalsIgnoreCase("desc")) {
            comparator = comparator.reversed();
        }

        return articles.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}

