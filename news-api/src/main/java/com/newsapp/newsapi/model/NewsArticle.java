package com.newsapp.newsapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsArticle implements Serializable {
    private String id;
    private String title;
    private String titleEs;
    private String description;
    private String descriptionEs;
    private String content;
    private String contentEs;
    private String author;
    private String sourceName;
    private String sourceUrl;
    private String imageUrl;
    private String articleUrl;
    private LocalDateTime publishedAt;
    private LocalDateTime fetchedAt;
}