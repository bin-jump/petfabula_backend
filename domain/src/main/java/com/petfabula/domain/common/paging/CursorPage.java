package com.petfabula.domain.common.paging;

import com.petfabula.domain.common.domain.EntityBase;
import lombok.Getter;

import java.util.List;

@Getter
public class CursorPage<T extends EntityBase> {

    public static <T extends EntityBase> CursorPage<T> of (List<T> res, boolean hasMore, int pageSize) {

        return new CursorPage(res, hasMore, pageSize);
    }

    private CursorPage(List<T> result, boolean hasMore, int pageSize) {

        this.result = result;
        this.hasMore = hasMore;
        this.pageSize = pageSize;

        if (hasMore && result.size() > 0) {
            nextCursor = result.get(result.size() - 1).getId();
        }
    }

    private List<T> result;

    private boolean hasMore;

    private int pageSize;

    private Long nextCursor;
}
