package com.petfabula.infrastructure.persistence.elasticsearch.config;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.petfabula.infrastructure.persistence.elasticsearch")
@Slf4j
public class SearchConfig {

    @Value("${spring.data.elasticsearch.host}")
    private String hostName;

    @Value("${spring.data.elasticsearch.restport}")
    private int portNumber;

    @Bean
    public RestHighLevelClient client() {
        String connectStr = hostName + ":" + portNumber;
        log.info("elastic elasticsearch: " + connectStr);

        ClientConfiguration clientConfiguration
                = ClientConfiguration.builder()
                .connectedTo(connectStr)
                .build();

        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(client());
    }

}
