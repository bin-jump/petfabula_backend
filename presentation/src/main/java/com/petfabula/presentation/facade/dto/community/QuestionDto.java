package com.petfabula.presentation.facade.dto.community;

import com.petfabula.presentation.facade.dto.ImageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionDto {

    private Long id;

    @NotBlank
    private String title;

    @NotNull
    private String content;

    private Integer upvoteCount;

    private Integer answerCount;

    private Long createdDate;

    private boolean upvoted;

    private ParticipatorDto participator;

    @Builder.Default
    private List<ImageDto> images = new ArrayList<>();
}

