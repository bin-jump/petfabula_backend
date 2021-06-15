package com.petfabula.infrastructure.persistence.elasticsearch.community;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.petfabula.domain.aggregate.community.post.PostSearchItem;
import com.petfabula.infrastructure.persistence.elasticsearch.ImageDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "post", shards = 2, replicas = 0, createIndex=true)
@Setting(settingPath = "/elasticsearch/settings.json")
public class PostDocument {

    public static PostDocument of(PostSearchItem postSearchItem) {
        PostDocument res = PostDocument.builder()
                .id(postSearchItem.getId())
                .relatePetId(postSearchItem.getRelatePetId())
                .content(postSearchItem.getContent())
                .coverImage(ImageDocument.of(postSearchItem.getCoverImage()))
                .version(postSearchItem.getVersion())
                .likeCount(postSearchItem.getLikeCount())
                .commentCount(postSearchItem.getCommentCount())
                .viewCount(postSearchItem.getViewCount())
                .petCategory(postSearchItem.getPetCategory())
                .createdDate(postSearchItem.getCreatedDate())
                .participator(ParticipatorDocument.of(postSearchItem.getParticipator()))
                .build();

        return res;
    }

    @Id
    private Long id;

    @Field(type = FieldType.Long)
    private Long relatePetId;

    @Field(type = FieldType.Text, analyzer = "ja_analyzer")
    private String content;

    @Field(type = FieldType.Nested, enabled = false)
    private ImageDocument coverImage;

    @Version
    private Long version;

    @Field(type = FieldType.Integer)
    private Integer likeCount;

    @Field(type = FieldType.Integer)
    private Integer commentCount;

    @Field(type = FieldType.Integer)
    private Integer viewCount;

    @Field(type = FieldType.Keyword)
    private String petCategory;

    @Field(type = FieldType.Date, format = DateFormat.date_optional_time)
    private Instant createdDate;

    @Field(type = FieldType.Nested)
    private ParticipatorDocument participator;
}
