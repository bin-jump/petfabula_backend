package com.petfabula.infrastructure.persistence.elasticsearch.community;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.petfabula.domain.aggregate.community.participator.ParticipatorSearchItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipatorDocument {

    public static ParticipatorDocument of (ParticipatorSearchItem item) {
        ParticipatorDocument res = ParticipatorDocument.builder()
                .id(item.getId())
                .name(item.getName())
                .photo(item.getPhoto())
                .build();

        return res;
    }

    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Keyword)
    private String photo;

}
