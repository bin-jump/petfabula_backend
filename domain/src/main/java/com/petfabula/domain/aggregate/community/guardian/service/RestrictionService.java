package com.petfabula.domain.aggregate.community.guardian.service;

import com.petfabula.domain.aggregate.community.guardian.entity.Restriction;
import com.petfabula.domain.aggregate.community.guardian.repository.RestrictionRepository;
import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorRepository;
import com.petfabula.domain.aggregate.community.post.service.PostIdGenerator;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestrictionService {

    @Autowired
    private ParticipatorRepository participatorRepository;

    @Autowired
    private RestrictionRepository restrictionRepository;

    @Autowired
    private PostIdGenerator idGenerator;

    public Restriction createPermanent(Long participatorId, String reason) {
        Participator participator = participatorRepository.findById(participatorId);
        if (participator == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        Long id = idGenerator.nextId();

        Restriction restriction = new Restriction(id, participatorId, reason);

        return restrictionRepository.save(restriction);
    }

    public void restrict(Long participatorId) {
        Restriction restriction = restrictionRepository.findByParticipatorId(participatorId);
        if (restriction == null) {
            return;
        }

        if (restriction.expired()) {
            restrictionRepository.remove(restriction);
            return;
        }

        throw new InvalidOperationException(CommonMessageKeys.ACTION_RESTRICTED);
    }

}
