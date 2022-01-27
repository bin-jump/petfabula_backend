package com.petfabula.presentation.facade.dto.administration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaticEmailCodeAccountDto {

    private Long id;

    private String name;

    private String photo;

    private String bio;

    private String email;

    @NotNull
    private String code;

    private boolean active;

}
