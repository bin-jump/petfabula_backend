package com.petfabula.application.event;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class PetRemoveEvent implements Serializable {

    private Long petId;
}
