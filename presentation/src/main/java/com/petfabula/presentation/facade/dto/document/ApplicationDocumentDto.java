package com.petfabula.presentation.facade.dto.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationDocumentDto {

    private Long id;

    @NotEmpty
    private String documentKey;

    @NotEmpty
    private String content;
}
