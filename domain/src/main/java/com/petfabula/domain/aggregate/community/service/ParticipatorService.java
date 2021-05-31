package com.petfabula.domain.aggregate.community.service;

import com.petfabula.domain.aggregate.community.entity.Participator;
import com.petfabula.domain.aggregate.community.entity.ParticipatorPet;
import com.petfabula.domain.aggregate.community.error.PostMessageKeys;
import com.petfabula.domain.aggregate.community.repository.ParticipatorPetRepository;
import com.petfabula.domain.aggregate.community.repository.ParticipatorRepository;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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

    public ParticipatorPet addParticipatorPet(Long petId, Long participatorId, String name, String photo) {
        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(PostMessageKeys.CANNOT_ADD_PET);
        }
        ParticipatorPet participatorPet = new ParticipatorPet(petId, participatorId, name, photo);
        participator.setPetCount(participator.getPostCount() + 1);
        participatorRepository.save(participator);
        return participatorPetRepository.save(participatorPet);
    }
}
