package com.petfabula.domain.common.paging;

import com.petfabula.domain.common.domain.EntityBase;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OffsetPage<T extends EntityBase> {

    public static <T extends EntityBase> OffsetPage<T> of (List<T> result, int pageIndex, int pageSize, boolean hasMore) {

        return new OffsetPage(result, pageIndex, pageSize, hasMore);
    }

    public static <T extends EntityBase> OffsetPage<T> ofEmpty (int pageIndex, int pageSize) {

        return new OffsetPage(new ArrayList(), pageIndex, pageSize, false);
    }

    public OffsetPage(List<T> result, int pageIndex, int pageSize, boolean hasMore) {
        this.result = result;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.hasMore = hasMore;
    }

    private List<T> result;

    private int pageIndex;

    private int pageSize;

    private boolean hasMore;

}
