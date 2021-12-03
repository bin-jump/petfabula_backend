package com.petfabula.domain.aggregate.identity.service.oauth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppleAuthContent {

    private String email;

    private String userId;
}
