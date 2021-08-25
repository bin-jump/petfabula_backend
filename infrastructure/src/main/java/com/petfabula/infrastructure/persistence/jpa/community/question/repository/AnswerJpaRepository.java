package com.petfabula.infrastructure.persistence.jpa.community.question.repository;

import com.petfabula.domain.aggregate.community.question.entity.Answer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AnswerJpaRepository extends JpaRepository<Answer, Long>, JpaSpecificationExecutor {

    @EntityGraph(value = "answer.all")
    List<Answer> findByIdInOrderByIdDesc(List<Long> ids);

    @EntityGraph(value = "answer.all")
    @Query("select a from Answer a where a.id = :id")
    Optional<Answer> findById(Long id);
}
