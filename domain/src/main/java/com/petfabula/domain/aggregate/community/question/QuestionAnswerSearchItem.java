package com.petfabula.domain.aggregate.community.question;

import com.petfabula.domain.aggregate.community.participator.ParticipatorSearchItem;
import com.petfabula.domain.common.search.SearchImageItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswerSearchItem {

    private Long id;

    private String title;

    private String content;

    private String answerContent;

    private SearchImageItem coverImage;

    private Long version;

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
