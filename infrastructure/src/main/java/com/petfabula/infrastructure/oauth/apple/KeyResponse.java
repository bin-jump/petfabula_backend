package com.petfabula.infrastructure.oauth.apple;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class KeyResponse {

    private AppleAuthKey[] keys;

}