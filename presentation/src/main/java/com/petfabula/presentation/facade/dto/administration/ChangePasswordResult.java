package com.petfabula.presentation.facade.dto.administration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordResult {

    public static ChangePasswordResult newInstance() {
        return new ChangePasswordResult(true);
    }

    private boolean changed;
}
