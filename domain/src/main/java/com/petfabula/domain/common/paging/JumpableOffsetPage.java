package com.petfabula.domain.common.paging;

import com.petfabula.domain.common.domain.EntityBase;
import lombok.Getter;

import java.util.List;

@Getter
public class JumpableOffsetPage<T extends EntityBase> {

    public static <T extends EntityBase> JumpableOffsetPage<T> of(List<T> result, int pageIndex, int pageSize, int total) {
        return new JumpableOffsetPage(result, pageIndex, pageSize, total);
    }

    public JumpableOffsetPage(List<T> result, int pageIndex, int pageSize, int total) {
        this.result = result;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.total = total;
    }

    private List<T> result;

    private int pageIndex;

    private int pageSize;

    private int total;
}
