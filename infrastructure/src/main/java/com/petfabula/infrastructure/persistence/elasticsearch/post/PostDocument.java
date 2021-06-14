package com.petfabula.infrastructure.persistence.elasticsearch.post;

import com.petfabula.domain.aggregate.community.post.PostSearchItem;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@Document(indexName = "post", shards = 2, replicas = 0, createIndex=true)
@Setting(settingPath = "/settings/settings.json")
public class PostDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Keyword)
    private Long relatePetId;

    private String content;

    private String coverImage;

    private PostSearchItem.PostSearchItemUser user;
}
