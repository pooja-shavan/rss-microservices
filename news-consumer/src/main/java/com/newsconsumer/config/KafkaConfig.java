package com.newsconsumer.config;
import com.newsapp.rssproducer.model.ArticleDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    private static final Logger log = LoggerFactory.getLogger(KafkaConfig.class);

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.consumer.auto-offset-reset:earliest}")
    private String autoOffsetReset;

    @Value("${spring.kafka.listener.concurrency:1}")
    private int concurrency;

    @Value("${spring.kafka.consumer.trusted-packages:com.newsapp.rssproducer.model}")
    private String trustedPackages;

    @Bean
    public ConsumerFactory<String, ArticleDto> consumerFactory() {
        try {
            Map<String, Object> props = new HashMap<>();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);

            JsonDeserializer<ArticleDto> deserializer = new JsonDeserializer<>(ArticleDto.class);
            deserializer.setRemoveTypeHeaders(false);
            deserializer.addTrustedPackages(trustedPackages);
            deserializer.setUseTypeMapperForKey(false);

            return new DefaultKafkaConsumerFactory<>(
                    props,
                    new StringDeserializer(),
                    deserializer
            );
        } catch (Exception e) {
            log.error("Failed to create Kafka ConsumerFactory", e);
            throw e;
        }
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ArticleDto> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ArticleDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(concurrency);
        return factory;
    }
}