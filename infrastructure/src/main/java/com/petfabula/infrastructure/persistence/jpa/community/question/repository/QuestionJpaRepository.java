package com.petfabula.infrastructure.persistence.jpa.community.question.repository;

import com.petfabula.domain.aggregate.community.question.entity.Question;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface QuestionJpaRepository extends JpaRepository<Question, Long>, JpaSpecificationExecutor {

    @EntityGraph(value = "question.all")
    List<Question> findByIdIn(List<Long> ids);
}
