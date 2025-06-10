package com.newsapp.newsapi.controller;

import com.newsapp.newsapi.model.Language;
import com.newsapp.newsapi.model.NewsResponse;
import com.newsapp.newsapi.service.NewsService;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/news")
@CrossOrigin
public class NewsController {

    private static final Logger log = LoggerFactory.getLogger(NewsController.class);
    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public NewsResponse getNews(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int pageSize,
            @RequestParam(defaultValue = "publishedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(defaultValue = "EN") Language language) {

        log.info("Fetching news: page={}, pageSize={}, sortBy={}, sortDir={}, language={}", page, pageSize, sortBy, sortDir, language);
        return newsService.getNews(page, pageSize, sortBy, sortDir, language);
    }
}