package com.petfabula.presentation.facade.dto.identity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailCodeRegisterRequest {

    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String code;

    private boolean userAgreement;

}
