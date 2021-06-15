package com.petfabula.domain.aggregate.community.participator.service;

import com.petfabula.domain.aggregate.community.participator.ParticipatorMessageKeys;
import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.participator.entity.ParticipatorPet;
import com.petfabula.domain.aggregate.community.participator.entity.PetCategory;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorPetRepository;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorRepository;
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
                                              PetCategory petCategory) {
        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(ParticipatorMessageKeys.CANNOT_ADD_PET);
        }
        ParticipatorPet participatorPet = new ParticipatorPet(petId, participatorId, name, photo, petCategory);
        participator.setPetCount(participator.getPostCount() + 1);
        participatorRepository.save(participator);
        return participatorPetRepository.save(participatorPet);
    }
}
