package com.newsapp.rssproducer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonToken;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for news articles.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleDto implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    @NotBlank
    private String id;

    @NotBlank
    private String title;

    private String description;
    private String content;
    private String author;
    private String sourceName;
    private String sourceUrl;
    private String imageUrl;
    private String articleUrl;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime publishedAt;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fetchedAt;

}

