package com.petfabula.presentation.facade.dto.identity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailCodeLoginRequest {

    @Email
    private String email;

    @NotNull
    private String code;

    public void setEmail(String value) {
        email = value != null ? value.trim() : null;
    }
}

