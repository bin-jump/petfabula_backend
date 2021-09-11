package com.petfabula.infrastructure.persistence.jpa.administration.repository;

import com.petfabula.domain.aggregate.administration.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackJpaRepository extends JpaRepository<Feedback, Long> {
}
