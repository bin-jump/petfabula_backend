package com.petfabula.domain.common.search;

import lombok.Getter;

import java.util.List;

@Getter
public class SearchAfterResult<T, CR> {

    public SearchAfterResult(CR cursor, List<T> result, boolean hasMore, int pageSize) {
        this.result = result;
        this.hasMore = hasMore;
        this.pageSize = pageSize;
        if (hasMore) {
            this.nextCursor = cursor;
        }
    }

    private List<T> result;

    private boolean hasMore;

    private int pageSize;

    private CR nextCursor;
}
