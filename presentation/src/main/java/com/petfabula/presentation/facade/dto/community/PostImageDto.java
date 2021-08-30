package com.petfabula.presentation.facade.dto.community;

import com.petfabula.presentation.facade.dto.ImageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostImageDto extends ImageDto {

    private Long postId;

    private Long petId;
}
