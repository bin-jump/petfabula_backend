package com.petfabula.presentation.facade.dto.community;

import com.petfabula.presentation.facade.dto.ImageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {

    private Long id;

    private Long relatePetId;

    private Long topicId;

    @NotBlank
    private String content;

    private Integer likeCount;

    private Integer commentCount;

    private Integer collectCount;

    private Long createdDate;

    private boolean liked;

    private boolean collected;

    private ParticipatorDto participator;

    private PostTopicDto postTopic;

    private ParticipatorPetDto relatePet;

    @Builder.Default
    private List<ImageDto> images = new ArrayList<>();
}

