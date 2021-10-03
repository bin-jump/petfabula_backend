package com.petfabula.presentation.facade.dto.community;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostTopicCategoryDto {

    private Long id;

    @NotEmpty
    private String title;

    private List<PostTopicDto> topics = new ArrayList<>();
}
