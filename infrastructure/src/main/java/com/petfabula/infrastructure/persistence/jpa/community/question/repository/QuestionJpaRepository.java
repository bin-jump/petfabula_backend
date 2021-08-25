package com.petfabula.infrastructure.persistence.jpa.community.question.repository;

import com.petfabula.domain.aggregate.community.question.entity.Question;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QuestionJpaRepository extends JpaRepository<Question, Long>, JpaSpecificationExecutor {

    @EntityGraph(value = "question.all")
    List<Question> findByIdInOrderByIdDesc(List<Long> ids);

    @EntityGraph(value = "question.all")
    @Query("select q from Question q where q.id = :id")
    Optional<Question> findById(Long id);
}
