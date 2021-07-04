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
public class AnswerDto {

    private Long id;

    @NotBlank
    private String content;

    @NotNull
    private Long questionId;

    private Integer upvoteCount;

    private Integer commentCount;

    private Long createdDate;

    private boolean upvoted;

    private ParticipatorDto participator;

    @Builder.Default
    private List<ImageDto> images = new ArrayList<>();
}
