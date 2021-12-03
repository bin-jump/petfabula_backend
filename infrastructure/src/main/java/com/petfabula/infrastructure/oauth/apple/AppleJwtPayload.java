package com.petfabula.infrastructure.oauth.apple;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppleJwtPayload {

    private String sub;

    private String email;

    private Boolean emailVerified;

    private Integer realUserStatus;
}
