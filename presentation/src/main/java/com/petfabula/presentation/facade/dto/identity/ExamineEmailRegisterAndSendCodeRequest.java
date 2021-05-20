package com.petfabula.presentation.facade.dto.identity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamineEmailRegisterAndSendCodeRequest {

    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;

    private boolean userAgreement;

}
