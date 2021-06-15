package com.petfabula.domain.common.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchImageItem {

    private String url;

    private Integer width;

    private Integer height;
}
