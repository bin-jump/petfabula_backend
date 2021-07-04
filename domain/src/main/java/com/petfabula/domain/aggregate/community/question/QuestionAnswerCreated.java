package com.petfabula.domain.aggregate.community.question;

import com.petfabula.domain.aggregate.community.participator.ParticipatorSearchItem;
import com.petfabula.domain.aggregate.community.question.entity.Answer;
import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.aggregate.community.question.entity.QuestionImage;
import com.petfabula.domain.common.domain.DomainEvent;
import com.petfabula.domain.common.search.SearchImageItem;
import lombok.Getter;

@Getter
public class QuestionAnswerCreated extends DomainEvent {

    private QuestionAnswerSearchItem questionAnswerSearchItem;

    public QuestionAnswerCreated(Question question) {
        SearchImageItem coverImage = null;
        if (question.getImages().size() > 0) {
            QuestionImage cimg = question.getImages().get(0);
            coverImage = new SearchImageItem(cimg.getUrl(),
                    cimg.getWidth(), cimg.getHeight());
        }

        ParticipatorSearchItem user = ParticipatorSearchItem.builder()
                .id(question.getParticipator().getId())
                .name(question.getParticipator().getName())
                .photo(question.getParticipator().getPhoto())
                .build();

        questionAnswerSearchItem = QuestionAnswerSearchItem.builder()
                .id(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .coverImage(coverImage)
                .version(question.getVersion())
                .upvoteCount(question.getUpvoteCount())
                .viewCount(question.getViewCount())
                .category(QuestionAnswerSearchItem.ItemType.QUESTION)
                .createdDate(question.getCreatedDate())
                .participator(user)
                .build();
    }

    public QuestionAnswerCreated(Answer answer, Question question) {

        ParticipatorSearchItem user = ParticipatorSearchItem.builder()
                .id(answer.getParticipator().getId())
                .name(answer.getParticipator().getName())
                .photo(answer.getParticipator().getPhoto())
                .build();

        questionAnswerSearchItem = QuestionAnswerSearchItem.builder()
                .id(answer.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .answerContent(answer.getContent())
                .version(answer.getVersion())
                .upvoteCount(answer.getUpvoteCount())
                .viewCount(answer.getViewCount())
                .category(QuestionAnswerSearchItem.ItemType.ANSWER)
                .createdDate(answer.getCreatedDate())
                .participator(user)
                .build();
    }

}
