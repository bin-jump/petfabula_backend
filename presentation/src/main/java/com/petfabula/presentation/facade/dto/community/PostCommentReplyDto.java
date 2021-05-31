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
public class PostCommentReplyDto {

    private Long id;

    private Long postId;

    @NotNull
    private Long postCommentId;

    @NotBlank
    private String content;

    private Long createdDate;

    private ParticipatorDto participator;
}
