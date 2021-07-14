package com.petfabula.presentation.facade.dto.community;

import com.petfabula.presentation.facade.dto.ImageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionAnswerSearchDto {

    private Long id;

    private Long questionId;

    private Long answerId;

    private String title;

    private String content;

    private String answerContent;

    private Integer upvoteCount;

    private Integer commentCount;

    private Integer viewCount;

    private Long createdDate;

    private ParticipatorDto participator;

    private String category;

    @Builder.Default
    private List<ImageDto> images = new ArrayList<>();
}
