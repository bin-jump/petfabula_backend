package com.petfabula.presentation.web.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
public class CursorPageData<T> {

    public static <T> CursorPageData<T> of(List<T> result, boolean hasMore,
                                           int pageSize, Object nextCursor) {
        return new CursorPageData(result, hasMore, pageSize, nextCursor);
    }

    public CursorPageData(List<T> result, boolean hasMore,
                          int pageSize, Object nextCursor) {
        this.result = result;
        this.pageSize = pageSize;
        this.hasMore = hasMore;
        if (hasMore) {
            this.nextCursor = nextCursor;
        }
    }

    private List<T> result;

    private boolean hasMore;

    private int pageSize;

    private Object nextCursor;
}
