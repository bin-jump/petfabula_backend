package com.petfabula.domain.common.search;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchQueryRequest<CR> {


    private String query;

    private int pageSize;

    private CR cursor;

    public boolean hasCursor() {
        return cursor != null;
    }
}
