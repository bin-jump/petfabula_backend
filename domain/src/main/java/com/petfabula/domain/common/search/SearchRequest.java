package com.petfabula.domain.common.search;

import lombok.Getter;

@Getter
public class SearchRequest {

    public SearchRequest(String query, int pageSize) {
        this.query = query;
        this.pageSize = pageSize;
    }

    private String query;

    private int pageSize;
}
