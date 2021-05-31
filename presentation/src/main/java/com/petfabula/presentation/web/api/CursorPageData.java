package com.petfabula.presentation.web.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CursorPageData<T> {

    public static <T> CursorPageData<T> of(List<T> result, boolean hasMore,
                                           int pageSize, Long nextCursor) {
        return new CursorPageData(result, hasMore, pageSize, nextCursor);
    }

    private List<T> result;

    private boolean hasMore;

    private int pageSize;

    private Long nextCursor;
}
