package com.petfabula.presentation.facade.dto.community;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostTopicDto {

    private Long id;

    private String title;

    private Long topicCategoryId;

    private String topicCategoryTitle;
}
