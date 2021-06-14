package com.petfabula.presentation.web.controller;

import com.petfabula.domain.aggregate.community.participator.entity.ParticipatorPet;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorPetRepository;
import com.petfabula.presentation.facade.assembler.community.ParticiptorPetAssembler;
import com.petfabula.presentation.facade.dto.community.ParticipatorPetDto;
import com.petfabula.presentation.web.api.Response;
import com.petfabula.presentation.web.security.LoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/participator")
@Validated
public class ParticipatorController {

    @Autowired
    private ParticiptorPetAssembler participtorPetAssembler;

    @Autowired
    private ParticipatorPetRepository participatorPetRepository;


    @GetMapping("pets")
    public Response<List<ParticipatorPetDto>> getMyPets() {
        Long userId = LoginUtils.currentUserId();
        List<ParticipatorPet> pets = participatorPetRepository.findByParticipatorId(userId);
        List<ParticipatorPetDto> res = participtorPetAssembler.convertToDtos(pets);
        return Response.ok(res);
    }
}
