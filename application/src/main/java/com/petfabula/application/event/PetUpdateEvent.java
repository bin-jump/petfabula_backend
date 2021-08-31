package com.petfabula.application.event;

import com.petfabula.domain.aggregate.pet.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class PetUpdateEvent implements Serializable {

    private Pet pet;
}

