package com.petfabula.presentation.web.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListData<T> {

    public static <T> ListData<T> of(List<T> result) {
        if (result == null) {
            result = new ArrayList<>();
        }
        return new ListData(result);
    }

    private List<T> result;
}
