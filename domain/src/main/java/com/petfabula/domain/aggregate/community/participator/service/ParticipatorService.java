package com.petfabula.domain.aggregate.community.participator.service;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.participator.entity.ParticipatorPet;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorPetRepository;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorRepository;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParticipatorService {

    @Autowired
    private ParticipatorRepository participatorRepository;

    @Autowired
    private ParticipatorPetRepository participatorPetRepository;

    public Participator createParticipator(Long id, String name, String photo) {
        Participator participator = participatorRepository.findById(id);
        if (participator != null) {
            return participator;
        }
        participator = new Participator(id, name, photo);
        return participatorRepository.save(participator);
    }

    public ParticipatorPet addParticipatorPet(Long petId, Long participatorId, String name, String photo,
                                              String petCategory, Long breedId) {
        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }
        ParticipatorPet participatorPet = new ParticipatorPet(petId, participatorId, name, photo, petCategory, breedId);
        participator.setPetCount(participator.getPostCount() + 1);
        participatorRepository.save(participator);
        return participatorPetRepository.save(participatorPet);
    }

    public ParticipatorPet updatePet(Long petId, Long participatorId, String name, String photo,
                                     String petCategory, Long breedId) {
        ParticipatorPet pet = participatorPetRepository.findById(petId);
        if (pet == null || !pet.getParticipatorId().equals(participatorId)) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }
        pet.setBreedId(breedId);
        pet.setCategory(petCategory);
        pet.setName(name);
        pet.setPhoto(photo);

        return pet;
    }

    public ParticipatorPet removePet(Long petId) {
        ParticipatorPet pet = participatorPetRepository.findById(petId);
        if (pet == null) {
            participatorPetRepository.remove(pet);
        }
        return pet;
    }
}
