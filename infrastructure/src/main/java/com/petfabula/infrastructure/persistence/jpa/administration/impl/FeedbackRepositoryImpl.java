package com.petfabula.infrastructure.persistence.jpa.administration.impl;

import com.petfabula.domain.aggregate.administration.entity.Feedback;
import com.petfabula.domain.aggregate.administration.repository.FeedbackRepository;
import com.petfabula.domain.common.paging.JumpableOffsetPage;
import com.petfabula.infrastructure.persistence.jpa.administration.repository.FeedbackJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class FeedbackRepositoryImpl implements FeedbackRepository {

    @Autowired
    private FeedbackJpaRepository feedbackJpaRepository;

    @Override
    public Feedback save(Feedback feedBack) {
        return feedbackJpaRepository.save(feedBack);
    }

    @Override
    public JumpableOffsetPage<Feedback> findAll(int pageIndex, int pageSize) {
        Pageable sortedById = PageRequest.of(pageIndex, pageSize, Sort.by("id").descending());
        Page<Feedback> reasonPage = feedbackJpaRepository.findAll(sortedById);

        int cnt = (int) reasonPage.getTotalElements();
        return JumpableOffsetPage.of(reasonPage.getContent(), pageIndex, pageSize, cnt);
    }
}
