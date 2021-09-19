package com.petfabula.domain.common.paging;

import com.petfabula.domain.common.domain.EntityBase;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CursorPage<T extends EntityBase> {

    public static <T extends EntityBase> CursorPage<T> of (List<T> res, boolean hasMore, int pageSize) {
        return new CursorPage(res, hasMore, pageSize);
    }

    public static <T extends EntityBase> CursorPage<T> of (List<T> res, Long nextCursor, int pageSize) {
        return new CursorPage(res, nextCursor, pageSize);
    }


    public static <T extends EntityBase> CursorPage<T> empty (int pageSize) {
        return new CursorPage(new ArrayList(), false, pageSize);
    }

    private CursorPage(List<T> result, boolean hasMore, int pageSize) {

        this.result = result;
        this.hasMore = hasMore;
        this.pageSize = pageSize;

        if (hasMore && result.size() > 0) {
            nextCursor = result.get(result.size() - 1).getId();
        }
    }

    private CursorPage(List<T> result, Long nextCursor, int pageSize) {

        this.result = result;
        this.nextCursor = nextCursor;
        this.pageSize = pageSize;

        if (nextCursor != null) {
            this.hasMore = true;
        }
    }

    private List<T> result;

    private boolean hasMore;

    private int pageSize;

    private Long nextCursor;
}
