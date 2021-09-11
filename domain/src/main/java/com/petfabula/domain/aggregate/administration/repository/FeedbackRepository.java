package com.petfabula.domain.aggregate.administration.repository;

import com.petfabula.domain.aggregate.administration.entity.Feedback;
import com.petfabula.domain.common.paging.JumpableOffsetPage;

public interface FeedbackRepository {

    Feedback save(Feedback feedBack);

    JumpableOffsetPage<Feedback> findAll(int pageIndex, int pageSize);
}
