package com.petfabula.presentation.facade.dto.community;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostCommentDto {

    private Long id;

    @NotNull
    private Long postId;

    @NotBlank
    private String content;

    private Integer replyCount;

    private Long createdDate;

    private ParticipatorDto participator;
}