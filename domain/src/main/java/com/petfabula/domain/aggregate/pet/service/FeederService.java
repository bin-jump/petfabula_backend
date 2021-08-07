package com.petfabula.domain.aggregate.pet.service;

import com.petfabula.domain.aggregate.pet.entity.Feeder;
import com.petfabula.domain.aggregate.pet.respository.FeederRepository;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeederService {

    @Autowired
    private FeederRepository feederRepository;

    public Feeder create(Long id, String name, String photo) {

        Feeder feeder = new Feeder(id, name, photo);
        return feederRepository.save(feeder);
    }

    public void setShowMedicalRecord(Long id, boolean show) {
        Feeder feeder = feederRepository.findById(id);
        if (feeder == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        feeder.setShowMedicalRecord(show);
    }

    public void setShowDisorderRecord(Long id, boolean show) {
        Feeder feeder = feederRepository.findById(id);
        if (feeder == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        feeder.setShowDisorderRecord(show);
    }

    public void setShowFeedRecord(Long id, boolean show) {
        Feeder feeder = feederRepository.findById(id);
        if (feeder == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        feeder.setShowFeedRecord(show);
    }

    public void setShowPetEventRecord(Long id, boolean show) {
        Feeder feeder = feederRepository.findById(id);
        if (feeder == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        feeder.setShowPetEventRecord(show);
    }

    public void setShowWeightRecord(Long id, boolean show) {
        Feeder feeder = feederRepository.findById(id);
        if (feeder == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        feeder.setShowWeightRecord(show);
    }

}
