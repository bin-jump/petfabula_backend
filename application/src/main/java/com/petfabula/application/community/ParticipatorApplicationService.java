package com.petfabula.application.community;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.participator.service.ParticipatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ParticipatorApplicationService {

    @Autowired
    private ParticipatorService participatorService;

    @Transactional
    public Participator createParticipator(Long id, String name, String photo){
        return participatorService.createParticipator(id, name, photo);
    }

}
