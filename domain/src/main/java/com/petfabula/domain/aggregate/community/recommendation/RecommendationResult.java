package com.petfabula.domain.aggregate.community.recommendation;

import com.petfabula.domain.common.domain.EntityBase;
import com.petfabula.domain.common.paging.OffsetPage;
import lombok.Getter;

@Getter
public class RecommendationResult<T extends EntityBase> {

    public RecommendationResult(OffsetPage<T> result, int seed, Long cursor) {
        this.result = result;
        this.seed = seed;
        this.cursor = cursor;
    }

    private OffsetPage<T> result;

    private int seed;

    private Long cursor;

}
