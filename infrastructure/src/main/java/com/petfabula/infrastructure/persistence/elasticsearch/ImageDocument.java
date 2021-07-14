package com.petfabula.infrastructure.persistence.elasticsearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.petfabula.domain.common.search.SearchImageItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDocument {

    public static ImageDocument of (SearchImageItem item) {
        if (item == null) {
            return null;
        }
        ImageDocument res = ImageDocument.builder()
                .url(item.getUrl())
                .width(item.getWidth())
                .height(item.getHeight())
                .build();
        return res;
    }

    public static List<ImageDocument> of (List<SearchImageItem> items) {
        return items.stream()
                .map(ImageDocument::of)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Field(type = FieldType.Keyword)
    private String url;

    @Field(type = FieldType.Integer)
    private Integer width;

    @Field(type = FieldType.Integer)
    private Integer height;
}
