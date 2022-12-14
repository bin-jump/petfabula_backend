package com.petfabula.presentation.facade.dto.community;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostTopicDto {

    private Long id;

    @NotEmpty
    private String title;

    @NotNull
    private Long topicCategoryId;

    private String topicCategoryTitle;
}
