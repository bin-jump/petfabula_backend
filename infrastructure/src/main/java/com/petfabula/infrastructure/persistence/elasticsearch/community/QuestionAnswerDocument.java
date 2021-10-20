package com.petfabula.infrastructure.persistence.elasticsearch.community;

import com.petfabula.domain.aggregate.community.question.QuestionAnswerSearchItem;
import com.petfabula.infrastructure.persistence.elasticsearch.ImageDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.common.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "question_answer", shards = 2, replicas = 0, createIndex=true)
@Setting(settingPath = "/elasticsearch/settings.json")
public class QuestionAnswerDocument {

    public static QuestionAnswerDocument of(QuestionAnswerSearchItem questionAnswerSearchItem) {

        QuestionAnswerDocument res = QuestionAnswerDocument.builder()
                .id(questionAnswerSearchItem.getId())
                .questionId(questionAnswerSearchItem.getQuestionId())
                .answerId(questionAnswerSearchItem.getAnswerId())
                .title(questionAnswerSearchItem.getTitle())
                .content(questionAnswerSearchItem.getContent())
                .answerContent(questionAnswerSearchItem.getAnswerContent())
                .images(ImageDocument.of(questionAnswerSearchItem.getImages()))
                .upvoteCount(questionAnswerSearchItem.getUpvoteCount())
                .commentCount(questionAnswerSearchItem.getCommentCount())
                .viewCount(questionAnswerSearchItem.getViewCount())
                .createdDate(questionAnswerSearchItem.getCreatedDate())
                .participator(ParticipatorDocument.of(questionAnswerSearchItem.getParticipator()))
                .category(questionAnswerSearchItem.getCategory().toString())
                .build();

        return res;
    }

    // Field annotation here is to fix id field creation,
    // otherwise id field will not be created until a document is inserted
    // TODO:  a better solution maybe ?
    @Id
    @Field
    private Long id;

    @Field(type = FieldType.Long)
    private Long questionId;

    @Field(type = FieldType.Long)
    private Long answerId;

    @Field(type = FieldType.Text, analyzer = "ja_analyzer")
    private String title;

    @Field(type = FieldType.Text, analyzer = "ja_analyzer")
    private String content;

    @Field(type = FieldType.Text, analyzer = "ja_analyzer")
    private String answerContent;

    @Field(type = FieldType.Nested, enabled = false)
    private List<ImageDocument> images;

    @Field(type = FieldType.Integer)
    private Integer upvoteCount;

    @Field(type = FieldType.Integer)
    private Integer commentCount;

    @Field(type = FieldType.Integer)
    private Integer viewCount;

    @Field(type = FieldType.Date, format = DateFormat.date_optional_time)
    private Instant createdDate;

    @Field(type = FieldType.Nested)
    private ParticipatorDocument participator;

    @Field(type = FieldType.Keyword)
    private String category;
}
