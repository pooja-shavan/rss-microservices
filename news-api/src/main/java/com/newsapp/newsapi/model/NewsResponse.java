package com.newsapp.newsapi.model;

import com.newsapp.rssproducer.model.ArticleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponse implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private List<ArticleDto> articles;
    private int totalResults;
    private int page;
    private int pageSize;
    private String sortBy;
    private Language language;
}

