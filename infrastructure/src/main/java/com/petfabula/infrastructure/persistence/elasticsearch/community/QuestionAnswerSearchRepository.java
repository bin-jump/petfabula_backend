package com.petfabula.infrastructure.persistence.elasticsearch.community;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface QuestionAnswerSearchRepository extends ElasticsearchRepository<QuestionAnswerDocument, Long> {
}
