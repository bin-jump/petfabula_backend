package com.petfabula.domain.aggregate.community.question;

import com.petfabula.domain.aggregate.community.participator.ParticipatorSearchItem;
import com.petfabula.domain.aggregate.community.question.entity.Answer;
import com.petfabula.domain.aggregate.community.question.entity.AnswerImage;
import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.aggregate.community.question.entity.QuestionImage;
import com.petfabula.domain.common.search.SearchImageItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswerSearchItem {

    public static List<SearchImageItem> fromQuestionImages(List<QuestionImage> images) {
        return images.stream()
                .map(item -> new SearchImageItem(item.getUrl(),
                        item.getWidth(), item.getHeight())).collect(Collectors.toList());
    }

    public static List<SearchImageItem> fromAnswerImages(List<AnswerImage> images) {
        return images.stream()
                .map(item -> new SearchImageItem(item.getUrl(),
                        item.getWidth(), item.getHeight())).collect(Collectors.toList());
    }

    public static QuestionAnswerSearchItem of(Question question, Long id) {

        QuestionAnswerSearchItem questionAnswerSearchItem =
                QuestionAnswerSearchItem.builder()
                .id(id)
                .questionId(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .images(fromQuestionImages(question.getImages()))
                .commentCount(question.getAnswerCount())
                .upvoteCount(question.getUpvoteCount())
                .viewCount(question.getViewCount())
                .category(ItemType.QUESTION)
                .createdDate(question.getCreatedDate())
                .participator(ParticipatorSearchItem.of(question.getParticipator()))
                .build();

        return questionAnswerSearchItem;
    }

    public static QuestionAnswerSearchItem of(Question question, Answer answer, Long id) {

        QuestionAnswerSearchItem questionAnswerSearchItem =
                QuestionAnswerSearchItem.builder()
                        .id(id)
                        .questionId(question.getId())
                        .answerId(answer.getId())
                        .title(question.getTitle())
                        .content(question.getContent())
                        .answerContent(answer.getContent())
                        .images(fromAnswerImages(answer.getImages()))
                        .commentCount(answer.getCommentCount())
                        .upvoteCount(answer.getUpvoteCount())
                        .viewCount(answer.getViewCount())
                        .category(ItemType.ANSWER)
                        .createdDate(answer.getCreatedDate())
                        .participator(ParticipatorSearchItem.of(answer.getParticipator()))
                        .build();

        return questionAnswerSearchItem;
    }

    private Long id;

    private Long questionId;

    private Long answerId;

    private String title;

    private String content;

    private String answerContent;

    private List<SearchImageItem> images;

    private Integer upvoteCount;

    private Integer commentCount;

    private Integer viewCount;

    private Instant createdDate;

    private ParticipatorSearchItem participator;

    private ItemType category;

    public enum ItemType {
        QUESTION, ANSWER
    }
}
