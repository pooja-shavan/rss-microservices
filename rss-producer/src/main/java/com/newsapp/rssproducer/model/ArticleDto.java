package com.newsapp.rssproducer.model;

import com.fasterxml.jackson.core.JsonToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleDto implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private String description;
    private String content;
    private String author;
    private String sourceName;
    private String sourceUrl;
    private String imageUrl;
    private String articleUrl;
    private LocalDateTime publishedAt;
    private LocalDateTime fetchedAt;

}

