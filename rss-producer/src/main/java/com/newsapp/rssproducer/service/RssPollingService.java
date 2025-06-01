package com.newsapp.rssproducer.service;

import com.newsapp.rssproducer.model.ArticleDto;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RssPollingService {

    private final KafkaTemplate<String, ArticleDto> kafkaTemplate;

    @Value("${rss.url}")
    private String rssUrl;

    @Value("${kafka.topic.articles}")
    private String articlesTopic;

    @Scheduled(fixedRateString = "${rss.polling.interval-ms: 300000}")
    public void pollRssFeed() {
        try {
            log.info("Polling RSS feed from {}", rssUrl);
            URL feedUrl = new URL(rssUrl);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));

            feed.getEntries().forEach(entry -> {
                ArticleDto article = convertToArticle(entry, feed);
                kafkaTemplate.send(articlesTopic, article.getId(), article);
            });
            log.info("Completed RSS polling. Processed {} entries", feed.getEntries().size());
        } catch (Exception e) {
            log.error("Error polling RSS feed: {}", e.getMessage(), e);
        }
    }

    private ArticleDto convertToArticle(SyndEntry entry, SyndFeed feed) {
        String description = entry.getDescription() != null ? entry.getDescription().getValue() : "";
        String content = entry.getContents().isEmpty() ? description :
                entry.getContents().get(0).getValue();

        // Extract image URL from content 
        String imageUrl = extractImageUrl(content);

        return ArticleDto.builder()
                .id(entry.getUri() != null ? entry.getUri() : UUID.randomUUID().toString())
                .title(entry.getTitle())
                .description(description)
                .content(content)
                .author(entry.getAuthor())
                .sourceName("New York Times")
                .sourceUrl(feed.getLink())
                .imageUrl(imageUrl)
                .articleUrl(entry.getLink())
                .publishedAt(convertToLocalDateTime(entry.getPublishedDate()))
                .fetchedAt(LocalDateTime.now())
                .build();
    }

    private String extractImageUrl(String content) {
        if (content == null) return null;

        // Look for img tags and extract src attribute
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("<img[^>]+src=\"([^\"]+)\"");
        java.util.regex.Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private LocalDateTime convertToLocalDateTime(Date date) {
        return date != null
                ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                : LocalDateTime.now();
    }
}
