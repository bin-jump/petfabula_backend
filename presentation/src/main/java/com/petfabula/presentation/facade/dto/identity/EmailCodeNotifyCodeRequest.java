package com.petfabula.presentation.facade.dto.identity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailCodeNotifyCodeRequest {

    @Email
    private String email;

}
