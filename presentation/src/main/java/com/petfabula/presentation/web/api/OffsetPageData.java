package com.petfabula.presentation.web.api;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
public class OffsetPageData<T> {

    public static <T> OffsetPageData<T> of(List<T> result, int page, int pageSize, int total) {
        return new OffsetPageData(result, page, pageSize, total);
    }

    public OffsetPageData(List<T> result, int page, int pageSize, int total) {
        this.result = result;
        this.page = page;
        this.size = pageSize;
        this.total = total;
    }

    private List<T> result;

    private int page;

    private int size;

    private int total;
}
